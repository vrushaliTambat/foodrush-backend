package com.vrush.service;

import com.vrush.dto.RestaurantDto;
import com.vrush.model.Restaurant;
import com.vrush.model.User;
import com.vrush.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user);

    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception;

    public void deleteRestaurant(Long restaurantId) throws Exception;

    public List<Restaurant>getAllRestaurant();

    public List<Restaurant> searchRestaurant(String keyword);

    public Restaurant findRestaurantById(Long id) throws Exception;

    public Restaurant getRestaurantsByUserId(Long userId) throws Exception;

    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception;

    public Restaurant updateRestaurantStatus(Long id)throws Exception;
}
