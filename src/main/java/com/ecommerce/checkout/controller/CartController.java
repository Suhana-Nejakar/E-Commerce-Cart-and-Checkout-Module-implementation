package com.ecommerce.checkout.controller;

import com.ecommerce.checkout.dto.request.AddCartItemRequest;
import com.ecommerce.checkout.dto.request.UpdateCartItemRequest;
import com.ecommerce.checkout.dto.response.CartResponse;
import com.ecommerce.checkout.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @Valid @RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(request));
    }

    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItem(userId, productId, request));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        cartService.removeItem(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> viewCart(
            @PathVariable Long userId) {
        return ResponseEntity.ok(cartService.viewCart(userId));
    }

    @PostMapping("/{userId}/coupon")
    public ResponseEntity<CartResponse> applyCoupon(
            @PathVariable Long userId,
            @RequestParam String code) {
        return ResponseEntity.ok(cartService.applyCoupon(userId, code));
    }
}
// vjnf