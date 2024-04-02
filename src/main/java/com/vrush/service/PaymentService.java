package com.vrush.service;

import com.stripe.exception.StripeException;
import com.vrush.model.Order;
import com.vrush.response.PaymentResponse;


public interface PaymentService {
    public PaymentResponse createPaymentLink(Order order) throws StripeException;

}
