package com.ecommerce.checkout.service;

import com.ecommerce.checkout.dto.request.CheckoutRequest;
import com.ecommerce.checkout.dto.response.CheckoutResponse;
import com.ecommerce.checkout.entity.*;
import com.ecommerce.checkout.enums.DiscountType;
import com.ecommerce.checkout.enums.PaymentStatus;
import com.ecommerce.checkout.repository.*;
import com.ecommerce.checkout.service.impl.CheckoutServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;
    @Mock private CartRepository cartRepository;
    @Mock private CouponRepository couponRepository;
    @Mock private OrderRepository orderRepository;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Test
    void checkoutSuccessShouldReduceInventoryAndReturnSuccess() {
        User user = User.builder().id(1L).name("Dhanaraj").email("d@example.com").build();
        Product product = Product.builder().id(1L).name("Phone").price(BigDecimal.valueOf(1000)).stockQuantity(10).build();
        Cart cart = Cart.builder().id(1L).user(user).items(new ArrayList<>()).build();
        CartItem item = CartItem.builder().id(1L).cart(cart).product(product).quantity(2).build();
        cart.getItems().add(item);

        Coupon coupon = Coupon.builder()
                .code("SAVE10")
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(BigDecimal.TEN)
                .expiryDate(LocalDate.now().plusDays(1))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));
        when(couponRepository.findById("SAVE10")).thenReturn(Optional.of(coupon));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(101L);
            return order;
        });

        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(1L);
        request.setCouponCode("SAVE10");
        request.setPaymentSuccess(true);

        CheckoutResponse response = checkoutService.checkout(request);

        assertEquals(PaymentStatus.SUCCESS, response.getPaymentStatus());
        assertEquals(8, product.getStockQuantity());
        assertEquals(BigDecimal.valueOf(200).setScale(2), response.getDiscountAmount());
        verify(productRepository).save(product);
        verify(cartRepository).save(cart);
    }

    @Test
    void checkoutPaymentFailureShouldNotReduceInventory() {
        User user = User.builder().id(1L).name("Dhanaraj").email("d@example.com").build();
        Product product = Product.builder().id(1L).name("Phone").price(BigDecimal.valueOf(1000)).stockQuantity(10).build();
        Cart cart = Cart.builder().id(1L).user(user).items(new ArrayList<>()).build();
        CartItem item = CartItem.builder().id(1L).cart(cart).product(product).quantity(2).build();
        cart.getItems().add(item);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(102L);
            return order;
        });

        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(1L);
        request.setPaymentSuccess(false);

        CheckoutResponse response = checkoutService.checkout(request);

        assertEquals(PaymentStatus.FAILED, response.getPaymentStatus());
        assertEquals(10, product.getStockQuantity());
        verify(productRepository, never()).save(product);
    }
}
