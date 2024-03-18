package com.vrush.service;

import com.vrush.model.Order;
import com.vrush.model.User;
import com.vrush.request.OrderRequest;

import java.util.List;

public class OrderServiceImpl implements OrderService{
    @Override
    public Order createOrder(OrderRequest req, User user) {
        return null;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        return null;
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {

    }

    @Override
    public List<Order> getUserOrders(Long userId) throws Exception {
        return null;
    }

    @Override
    public List<Order> getRestaurantOrders(Long restaurantId, String orderStatus) throws Exception {
        return null;
    }
}
