package com.ecommerce.checkout.dto.response;

import java.math.BigDecimal;

public class CartItemResponse {
    private Long productId; private String productName; private BigDecimal price; private Integer quantity; private BigDecimal lineTotal;
    public CartItemResponse() {}
    public CartItemResponse(Long productId, String productName, BigDecimal price, Integer quantity, BigDecimal lineTotal){this.productId=productId;this.productName=productName;this.price=price;this.quantity=quantity;this.lineTotal=lineTotal;}
    public Long getProductId(){return productId;} public void setProductId(Long productId){this.productId=productId;} public String getProductName(){return productName;} public void setProductName(String productName){this.productName=productName;} public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal price){this.price=price;} public Integer getQuantity(){return quantity;} public void setQuantity(Integer quantity){this.quantity=quantity;} public BigDecimal getLineTotal(){return lineTotal;} public void setLineTotal(BigDecimal lineTotal){this.lineTotal=lineTotal;}
    public static Builder builder(){return new Builder();}
    public static class Builder{private Long productId;private String productName;private BigDecimal price;private Integer quantity;private BigDecimal lineTotal; public Builder productId(Long productId){this.productId=productId;return this;} public Builder productName(String productName){this.productName=productName;return this;} public Builder price(BigDecimal price){this.price=price;return this;} public Builder quantity(Integer quantity){this.quantity=quantity;return this;} public Builder lineTotal(BigDecimal lineTotal){this.lineTotal=lineTotal;return this;} public CartItemResponse build(){return new CartItemResponse(productId,productName,price,quantity,lineTotal);} }
}
