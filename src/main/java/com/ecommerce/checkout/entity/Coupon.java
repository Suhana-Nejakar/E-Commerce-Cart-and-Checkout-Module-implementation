package com.ecommerce.checkout.entity;

import com.ecommerce.checkout.enums.DiscountType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id private String code;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private DiscountType discountType;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal discountValue;
    @Column(nullable = false) private LocalDate expiryDate;
    public Coupon() {}
    public Coupon(String code, DiscountType discountType, BigDecimal discountValue, LocalDate expiryDate){this.code=code;this.discountType=discountType;this.discountValue=discountValue;this.expiryDate=expiryDate;}
    public String getCode(){return code;} public void setCode(String code){this.code=code;} public DiscountType getDiscountType(){return discountType;} public void setDiscountType(DiscountType discountType){this.discountType=discountType;} public BigDecimal getDiscountValue(){return discountValue;} public void setDiscountValue(BigDecimal discountValue){this.discountValue=discountValue;} public LocalDate getExpiryDate(){return expiryDate;} public void setExpiryDate(LocalDate expiryDate){this.expiryDate=expiryDate;}
    public static Builder builder(){return new Builder();}
    public static class Builder{private String code;private DiscountType discountType;private BigDecimal discountValue;private LocalDate expiryDate; public Builder code(String code){this.code=code;return this;} public Builder discountType(DiscountType discountType){this.discountType=discountType;return this;} public Builder discountValue(BigDecimal discountValue){this.discountValue=discountValue;return this;} public Builder expiryDate(LocalDate expiryDate){this.expiryDate=expiryDate;return this;} public Coupon build(){return new Coupon(code,discountType,discountValue,expiryDate);} }
}
