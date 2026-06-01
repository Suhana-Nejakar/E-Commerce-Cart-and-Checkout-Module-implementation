package com.ecommerce.checkout.service.impl;

import com.ecommerce.checkout.dto.response.OrderHistoryResponse;
import com.ecommerce.checkout.repository.OrderRepository;
import com.ecommerce.checkout.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<OrderHistoryResponse> getOrderHistory(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(order -> OrderHistoryResponse.builder()
                        .orderId(order.getId())
                        .finalAmount(order.getFinalAmount())
                        .orderStatus(order.getStatus())
                        .paymentStatus(order.getPaymentStatus())
                        .createdAt(order.getCreatedAt())
                        .build());
    }
}
