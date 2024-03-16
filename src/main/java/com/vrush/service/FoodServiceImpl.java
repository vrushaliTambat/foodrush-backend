package com.vrush.service;

import com.vrush.model.Category;
import com.vrush.model.Food;
import com.vrush.model.Restaurant;
import com.vrush.repository.FoodRepository;
import com.vrush.request.CreateFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService{

    @Autowired
    private FoodRepository foodRepository;
    @Override
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) throws Exception {
        Food food=new Food();
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);
        food.setDescription(req.getDescription());
        food.setImages(req.getImages());
        food.setName(req.getName());
        food.setPrice(req.getPrice());
        food.setSeasonal(req.isSeasonal());
        food.setVegetarian(req.isVegetarian());
        food.setIngredients(req.getIngredients());

        Food savedFood= foodRepository.save(food);
        restaurant.getFoods().add(food);

        return savedFood;
    }

    @Override
    public void deleteFood(Long foodId) throws Exception {
        Food food=findFoodById(foodId);
    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId, boolean isVegetarian, boolean isNonveg, boolean isSeasonal, String foodCategory) throws Exception {
        return null;
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return null;
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        return null;
    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) throws Exception {
        return null;
    }
}
