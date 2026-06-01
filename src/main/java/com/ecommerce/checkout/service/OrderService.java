package com.ecommerce.checkout.service;

import com.ecommerce.checkout.dto.response.OrderHistoryResponse;
import org.springframework.data.domain.Page;

public interface OrderService {
    Page<OrderHistoryResponse> getOrderHistory(Long userId, int page, int size);
}
