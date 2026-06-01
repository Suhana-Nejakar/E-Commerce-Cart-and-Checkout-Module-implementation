package com.ecommerce.checkout.controller;

import com.ecommerce.checkout.dto.response.OrderHistoryResponse;
import com.ecommerce.checkout.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderHistoryResponse>> getOrderHistory(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getOrderHistory(userId, page, size));
    }
}
