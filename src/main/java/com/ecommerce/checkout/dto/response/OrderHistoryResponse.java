package com.ecommerce.checkout.dto.response;

import com.ecommerce.checkout.enums.OrderStatus;
import com.ecommerce.checkout.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderHistoryResponse {
    private Long orderId; private BigDecimal finalAmount; private OrderStatus orderStatus; private PaymentStatus paymentStatus; private LocalDateTime createdAt;
    public OrderHistoryResponse() {}
    public OrderHistoryResponse(Long orderId, BigDecimal finalAmount, OrderStatus orderStatus, PaymentStatus paymentStatus, LocalDateTime createdAt){this.orderId=orderId;this.finalAmount=finalAmount;this.orderStatus=orderStatus;this.paymentStatus=paymentStatus;this.createdAt=createdAt;}
    public Long getOrderId(){return orderId;} public void setOrderId(Long orderId){this.orderId=orderId;} public BigDecimal getFinalAmount(){return finalAmount;} public void setFinalAmount(BigDecimal finalAmount){this.finalAmount=finalAmount;} public OrderStatus getOrderStatus(){return orderStatus;} public void setOrderStatus(OrderStatus orderStatus){this.orderStatus=orderStatus;} public PaymentStatus getPaymentStatus(){return paymentStatus;} public void setPaymentStatus(PaymentStatus paymentStatus){this.paymentStatus=paymentStatus;} public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
    public static Builder builder(){return new Builder();}
    public static class Builder{private Long orderId;private BigDecimal finalAmount;private OrderStatus orderStatus;private PaymentStatus paymentStatus;private LocalDateTime createdAt; public Builder orderId(Long orderId){this.orderId=orderId;return this;} public Builder finalAmount(BigDecimal finalAmount){this.finalAmount=finalAmount;return this;} public Builder orderStatus(OrderStatus orderStatus){this.orderStatus=orderStatus;return this;} public Builder paymentStatus(PaymentStatus paymentStatus){this.paymentStatus=paymentStatus;return this;} public Builder createdAt(LocalDateTime createdAt){this.createdAt=createdAt;return this;} public OrderHistoryResponse build(){return new OrderHistoryResponse(orderId,finalAmount,orderStatus,paymentStatus,createdAt);} }
}
