package com.ecommerce.checkout.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String name;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal price;
    @Column(nullable = false) private Integer stockQuantity;
    public Product() {}
    public Product(Long id, String name, BigDecimal price, Integer stockQuantity){this.id=id;this.name=name;this.price=price;this.stockQuantity=stockQuantity;}
    public Long getId(){return id;} public void setId(Long id){this.id=id;} public String getName(){return name;} public void setName(String name){this.name=name;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal price){this.price=price;} public Integer getStockQuantity(){return stockQuantity;} public void setStockQuantity(Integer stockQuantity){this.stockQuantity=stockQuantity;}
    public static Builder builder(){return new Builder();}
    public static class Builder{private Long id;private String name;private BigDecimal price;private Integer stockQuantity;
        public Builder id(Long id){this.id=id;return this;} public Builder name(String name){this.name=name;return this;} public Builder price(BigDecimal price){this.price=price;return this;} public Builder stockQuantity(Integer stockQuantity){this.stockQuantity=stockQuantity;return this;} public Product build(){return new Product(id,name,price,stockQuantity);} }
}
