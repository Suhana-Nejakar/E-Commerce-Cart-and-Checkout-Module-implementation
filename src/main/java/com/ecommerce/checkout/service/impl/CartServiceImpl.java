package com.ecommerce.checkout.service.impl;

import com.ecommerce.checkout.dto.request.AddCartItemRequest;
import com.ecommerce.checkout.dto.request.UpdateCartItemRequest;
import com.ecommerce.checkout.dto.response.CartItemResponse;
import com.ecommerce.checkout.dto.response.CartResponse;
import com.ecommerce.checkout.entity.Cart;
import com.ecommerce.checkout.entity.CartItem;
import com.ecommerce.checkout.entity.Coupon;
import com.ecommerce.checkout.entity.Product;
import com.ecommerce.checkout.entity.User;
import com.ecommerce.checkout.enums.DiscountType;
import com.ecommerce.checkout.exception.ResourceNotFoundException;
import com.ecommerce.checkout.repository.CartItemRepository;
import com.ecommerce.checkout.repository.CartRepository;
import com.ecommerce.checkout.repository.CouponRepository;
import com.ecommerce.checkout.repository.ProductRepository;
import com.ecommerce.checkout.repository.UserRepository;
import com.ecommerce.checkout.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;

    public CartServiceImpl(
            UserRepository userRepository,
            ProductRepository productRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            CouponRepository couponRepository
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    @Transactional
    public CartResponse addItem(AddCartItemRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(0);
        }

        item.setQuantity(item.getQuantity() + request.getQuantity());
        cartItemRepository.save(item);

        return viewCart(user.getId());
    }

    @Override
    @Transactional
    public CartResponse updateItem(
            Long userId,
            Long productId,
            UpdateCartItemRequest request
    ) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        return viewCart(userId);
    }

    @Override
    @Transactional
    public void removeItem(Long userId, Long productId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse viewCart(Long userId) {

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return buildCartResponse(cart, userId, null);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse applyCoupon(Long userId, String code) {

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid coupon code"));

        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Coupon expired");
        }

        return buildCartResponse(cart, userId, coupon);
    }

    private CartResponse buildCartResponse(
            Cart cart,
            Long userId,
            Coupon coupon
    ) {

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(this::toCartItemResponse)
                .toList();

        BigDecimal totalAmount = items.stream()
                .map(CartItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountAmount = BigDecimal.ZERO;

        if (coupon != null) {
            discountAmount = calculateDiscount(totalAmount, coupon);
        }

        BigDecimal finalAmount = totalAmount.subtract(discountAmount);

        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());
        response.setUserId(userId);
        response.setItems(items);
        response.setTotalAmount(totalAmount);

        // Add these fields in CartResponse DTO if not present
        response.setCouponCode(coupon != null ? coupon.getCode() : null);
        response.setDiscountAmount(discountAmount);
        response.setFinalAmount(finalAmount);

        return response;
    }

    private BigDecimal calculateDiscount(
            BigDecimal totalAmount,
            Coupon coupon
    ) {

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            return totalAmount
                    .multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100));
        }

        return coupon.getDiscountValue();
    }

    private CartItemResponse toCartItemResponse(CartItem item) {

        Product product = item.getProduct();

        BigDecimal lineTotal = product.getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        CartItemResponse response = new CartItemResponse();
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setPrice(product.getPrice());
        response.setQuantity(item.getQuantity());
        response.setLineTotal(lineTotal);

        return response;
    }
}