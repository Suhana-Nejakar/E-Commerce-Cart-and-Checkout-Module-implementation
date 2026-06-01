package com.ecommerce.checkout.entity;

import com.ecommerce.checkout.enums.OrderStatus;
import com.ecommerce.checkout.enums.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false) private User user;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal totalAmount;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal discountAmount;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal finalAmount;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private OrderStatus status;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private PaymentStatus paymentStatus;
    @Column(nullable = false) private LocalDateTime createdAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) private List<OrderItem> items = new ArrayList<>();
    public Order() {}
    public Order(Long id, User user, BigDecimal totalAmount, BigDecimal discountAmount, BigDecimal finalAmount, OrderStatus status, PaymentStatus paymentStatus, LocalDateTime createdAt, List<OrderItem> items){this.id=id;this.user=user;this.totalAmount=totalAmount;this.discountAmount=discountAmount;this.finalAmount=finalAmount;this.status=status;this.paymentStatus=paymentStatus;this.createdAt=createdAt;this.items=items != null ? items : new ArrayList<>();}
    public Long getId(){return id;} public void setId(Long id){this.id=id;} public User getUser(){return user;} public void setUser(User user){this.user=user;} public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal totalAmount){this.totalAmount=totalAmount;} public BigDecimal getDiscountAmount(){return discountAmount;} public void setDiscountAmount(BigDecimal discountAmount){this.discountAmount=discountAmount;} public BigDecimal getFinalAmount(){return finalAmount;} public void setFinalAmount(BigDecimal finalAmount){this.finalAmount=finalAmount;} public OrderStatus getStatus(){return status;} public void setStatus(OrderStatus status){this.status=status;} public PaymentStatus getPaymentStatus(){return paymentStatus;} public void setPaymentStatus(PaymentStatus paymentStatus){this.paymentStatus=paymentStatus;} public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;} public List<OrderItem> getItems(){return items;} public void setItems(List<OrderItem> items){this.items=items;}
    public static Builder builder(){return new Builder();}
    public static class Builder{private Long id;private User user;private BigDecimal totalAmount;private BigDecimal discountAmount;private BigDecimal finalAmount;private OrderStatus status;private PaymentStatus paymentStatus;private LocalDateTime createdAt;private List<OrderItem> items = new ArrayList<>(); public Builder id(Long id){this.id=id;return this;} public Builder user(User user){this.user=user;return this;} public Builder totalAmount(BigDecimal totalAmount){this.totalAmount=totalAmount;return this;} public Builder discountAmount(BigDecimal discountAmount){this.discountAmount=discountAmount;return this;} public Builder finalAmount(BigDecimal finalAmount){this.finalAmount=finalAmount;return this;} public Builder status(OrderStatus status){this.status=status;return this;} public Builder paymentStatus(PaymentStatus paymentStatus){this.paymentStatus=paymentStatus;return this;} public Builder createdAt(LocalDateTime createdAt){this.createdAt=createdAt;return this;} public Builder items(List<OrderItem> items){this.items=items;return this;} public Order build(){return new Order(id,user,totalAmount,discountAmount,finalAmount,status,paymentStatus,createdAt,items);} }
}
