package com.vrush.controller;

import com.vrush.model.Order;
import com.vrush.model.User;
import com.vrush.service.OrderService;
import com.vrush.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    //where this id is restaurantId
    @GetMapping("/order/restaurant/{id}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable Long id,
                                                       @RequestParam(required = false) String order_status,
                                                       @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders= orderService.getRestaurantOrders(id,order_status);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @PutMapping("/orders/{orderId}/{orderStatus}")
    public ResponseEntity<Order> updateOrdersStatus(@PathVariable Long orderId,
                                                          @PathVariable String orderStatus,
                                                          @RequestHeader("Authorization") String jwt) throws Exception{

        User user = userService.findUserByJwtToken(jwt);
        Order orders = orderService.updateOrder(orderId, orderStatus);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}

