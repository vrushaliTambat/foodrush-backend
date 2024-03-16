package com.vrush.service;

import com.vrush.model.Category;
import com.vrush.model.Food;
import com.vrush.model.Restaurant;
import com.vrush.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {
    public Food createFood(CreateFoodRequest req, Category category,
                           Restaurant restaurant) throws Exception;

    void deleteFood(Long foodId) throws Exception;
    public List<Food> getRestaurantsFood(Long restaurantId, boolean isVegetarian,
                                         boolean isNonveg, boolean isSeasonal,
                                         String foodCategory) throws Exception;

    public List<Food> searchFood(String keyword);

    public Food findFoodById(Long foodId) throws Exception;

    public Food updateAvailabilityStatus(Long foodId) throws Exception;
}
