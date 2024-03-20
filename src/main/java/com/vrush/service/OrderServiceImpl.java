package com.vrush.service;

import com.vrush.model.*;
import com.vrush.repository.AddressRepository;
import com.vrush.repository.OrderItemRepository;
import com.vrush.repository.OrderRepository;
import com.vrush.repository.UserRepository;
import com.vrush.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderItemRepository orderItemRepo;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CartService cartService;

    @Override
    public Order createOrder(OrderRequest order, User user) throws Exception {

        Address shipAddress = order.getDeliveryAddress();
        Address savedAddress = addressRepo.save(shipAddress);

        //if address not already present then adding it to the user's address list
        if(!user.getAddresses().contains(savedAddress)) {
            user.getAddresses().add(savedAddress);
            userRepo.save(user);
        }
       Restaurant restaurant=restaurantService.findRestaurantById(order.getRestaurantId());

        Order createdOrder = new Order();
        createdOrder.setCustomer(user);
        createdOrder.setDeliveryAddress(savedAddress);
        createdOrder.setCreatedAt(new Date());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.setRestaurant(restaurant);

        Cart cart = cartService.findCartByUserId(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            OrderItem item = new OrderItem();
            item.setFood(cartItem.getFood());
            item.setIngredients(cartItem.getIngredients());
            item.setQuantity(cartItem.getQuantity());
            item.setTotalPrice(cartItem.getTotalPrice());

            OrderItem savedOrderItem = orderItemRepo.save(item);
            orderItems.add(savedOrderItem);
        }
        Long totalPrice = cartService.calculateCartTotal(cart);
        createdOrder.setItems(orderItems);
        createdOrder.setTotalPrice(totalPrice);

        Order savedOrder=orderRepo.save(createdOrder);
        restaurant.getOrders().add(savedOrder);
        return createdOrder;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        Order order=findOrderById(orderId);
        if(orderStatus.equals("OUT_FOR_DELIVERY") || orderStatus.equals("DELIVERED")
                || orderStatus.equals("PENDING") || orderStatus.equals("COMPLETED")){
            order.setOrderStatus(orderStatus);
            return orderRepo.save(order);
        }
       throw new Exception("Please select a valid order status.");
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {
        Order order=findOrderById(orderId);
        orderRepo.deleteById(orderId);
    }

    @Override
    public List<Order> getUserOrders(Long userId) throws Exception {

        return orderRepo.findByCustomerId(userId);
    }

    @Override
    public List<Order> getRestaurantOrders(Long restaurantId, String orderStatus) throws Exception {
        List<Order> orders= orderRepo.findByRestaurantId(restaurantId);
        //if orderStatus is not null then it filters accd to the orderStatus th orders
        if(orderStatus!=null){
            orders = orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());

        }
        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        if(optionalOrder.isEmpty()){
            throw new Exception("Order not found with the id "+orderId);
        }
        return optionalOrder.get();
    }
}
