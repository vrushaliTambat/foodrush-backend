package com.vrush.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vrush.model.Order;
import com.vrush.response.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Value("${api.key}")
    private String stripeSecretKey;

    @Override
    public PaymentResponse createPaymentLink(Order order) throws StripeException {
        Stripe.apiKey=stripeSecretKey;

        SessionCreateParams params=SessionCreateParams.builder().addPaymentMethodType(
                        SessionCreateParams.
                        PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://food-rush-vrushali-tambats-projects.vercel.app/payment/success/"+order.getId())
                .setCancelUrl("https://food-rush-vrushali-tambats-projects.vercel.app/payment/fail")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) order.getTotalPrice()*100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Food Rush")
                                        .build())
                                .build()
                        )
                        .build()
                )
                .build();

        //using params we'll create a session
        Session session=Session.create(params);
        PaymentResponse res=new PaymentResponse();
        //session.getUrl() returns the URL of the payment session created using the Stripe API, which customers will visit to complete their payment.
        res.setPayment_url(session.getUrl());

        return res;
    }
}
