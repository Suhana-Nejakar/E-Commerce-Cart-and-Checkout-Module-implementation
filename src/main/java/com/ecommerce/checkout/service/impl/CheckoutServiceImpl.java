package com.ecommerce.checkout.service.impl;

import com.ecommerce.checkout.dto.request.CheckoutRequest;
import com.ecommerce.checkout.dto.response.CartItemResponse;
import com.ecommerce.checkout.dto.response.CheckoutResponse;
import com.ecommerce.checkout.entity.*;
import com.ecommerce.checkout.enums.DiscountType;
import com.ecommerce.checkout.enums.OrderStatus;
import com.ecommerce.checkout.enums.PaymentStatus;
import com.ecommerce.checkout.exception.BadRequestException;
import com.ecommerce.checkout.exception.InsufficientStockException;
import com.ecommerce.checkout.exception.ResourceNotFoundException;
import com.ecommerce.checkout.repository.*;
import com.ecommerce.checkout.service.CheckoutService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    public CheckoutServiceImpl(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, CouponRepository couponRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.couponRepository = couponRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // Lock product rows and validate stock before payment/order confirmation.
        for (CartItem item : cart.getItems()) {
            Product lockedProduct = productRepository.findByIdForUpdate(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (lockedProduct.getStockQuantity() < item.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + lockedProduct.getName());
            }
            item.setProduct(lockedProduct);
        }

        BigDecimal totalAmount = calculateTotal(cart);
        BigDecimal discountAmount = calculateDiscount(totalAmount, request.getCouponCode());
        BigDecimal finalAmount = totalAmount.subtract(discountAmount).max(BigDecimal.ZERO);

        boolean paymentSuccess = Boolean.TRUE.equals(request.getPaymentSuccess());
        PaymentStatus paymentStatus = paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
        OrderStatus orderStatus = paymentSuccess ? OrderStatus.CONFIRMED : OrderStatus.FAILED;

        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .paymentStatus(paymentStatus)
                .status(orderStatus)
                .createdAt(LocalDateTime.now())
                .build();

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getProduct().getPrice())
                    .build();
            order.getItems().add(orderItem);
        }

        if (paymentSuccess) {
            for (CartItem cartItem : cart.getItems()) {
                Product product = cartItem.getProduct();
                product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
                productRepository.save(product);
            }
            cart.getItems().clear();
            cartRepository.save(cart);
        }

        Order savedOrder = orderRepository.save(order);

        return CheckoutResponse.builder()
                .orderId(savedOrder.getId())
                .userId(user.getId())
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .paymentStatus(paymentStatus)
                .orderStatus(orderStatus)
                .items(savedOrder.getItems().stream().map(this::toCartItemResponse).toList())
                .build();
    }

    private BigDecimal calculateTotal(Cart cart) {
        return cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDiscount(BigDecimal totalAmount, String couponCode) {
        if (couponCode == null || couponCode.isBlank()) {
            return BigDecimal.ZERO;
        }

        Coupon coupon = couponRepository.findById(couponCode)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid coupon code"));

        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Coupon expired");
        }

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            return totalAmount.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        return coupon.getDiscountValue().min(totalAmount);
    }

    private CartItemResponse toCartItemResponse(OrderItem item) {
        BigDecimal lineTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return CartItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .lineTotal(lineTotal)
                .build();
    }
}
