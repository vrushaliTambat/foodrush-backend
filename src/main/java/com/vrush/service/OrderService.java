package com.vrush.service;

import com.vrush.model.Order;
import com.vrush.model.User;
import com.vrush.request.OrderRequest;

import java.util.List;

public interface OrderService {
    public Order createOrder(OrderRequest req, User user) throws Exception;
    public Order updateOrder(Long orderId,String orderStatus) throws Exception;
    public void cancelOrder(Long orderId) throws Exception;
    public List<Order> getUserOrders(Long userId) throws Exception;
    public List<Order> getRestaurantOrders(Long restaurantId,String orderStatus) throws Exception;

    public Order findOrderById(Long orderId) throws Exception;

}
