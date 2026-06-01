package com.ecommerce.checkout.dto.request;

import jakarta.validation.constraints.NotNull;

public class CheckoutRequest {
    @NotNull private Long userId;
    private String couponCode;
    private Boolean paymentSuccess = true;
    public CheckoutRequest() {}
    public Long getUserId(){return userId;} public void setUserId(Long userId){this.userId=userId;} public String getCouponCode(){return couponCode;} public void setCouponCode(String couponCode){this.couponCode=couponCode;} public Boolean getPaymentSuccess(){return paymentSuccess;} public void setPaymentSuccess(Boolean paymentSuccess){this.paymentSuccess=paymentSuccess;}
}
