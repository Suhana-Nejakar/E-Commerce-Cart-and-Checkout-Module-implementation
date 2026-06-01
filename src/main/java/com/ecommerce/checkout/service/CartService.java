package com.ecommerce.checkout.service;

import com.ecommerce.checkout.dto.request.AddCartItemRequest;
import com.ecommerce.checkout.dto.request.UpdateCartItemRequest;
import com.ecommerce.checkout.dto.response.CartResponse;

public interface CartService {

    CartResponse addItem(AddCartItemRequest request);

    CartResponse updateItem(
            Long userId,
            Long productId,
            UpdateCartItemRequest request
    );

    void removeItem(Long userId, Long productId);

    CartResponse viewCart(Long userId);

    CartResponse applyCoupon(Long userId, String code);
}