package com.ecommerce.checkout.service;

import com.ecommerce.checkout.dto.request.CheckoutRequest;
import com.ecommerce.checkout.dto.response.CheckoutResponse;

public interface CheckoutService {
    CheckoutResponse checkout(CheckoutRequest request);
}
