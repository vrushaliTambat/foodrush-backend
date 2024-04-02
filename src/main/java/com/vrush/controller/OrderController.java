package com.vrush.controller;

import com.vrush.model.Order;
import com.vrush.model.User;
import com.vrush.request.OrderRequest;
import com.vrush.response.PaymentResponse;
import com.vrush.service.OrderService;
import com.vrush.service.PaymentService;
import com.vrush.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;

    @PostMapping("/order")
    public ResponseEntity<PaymentResponse>  createOrder(@RequestBody OrderRequest req,
                                                        @RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Order order= orderService.createOrder(req,user);
        PaymentResponse res=paymentService.createPaymentLink(order);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrderHistory(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders= orderService.getUserOrders(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
