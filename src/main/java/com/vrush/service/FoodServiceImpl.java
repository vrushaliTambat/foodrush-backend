package com.vrush.service;

import com.vrush.model.Category;
import com.vrush.model.Food;
import com.vrush.model.Restaurant;
import com.vrush.repository.FoodRepository;
import com.vrush.request.CreateFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        //removing the food from restaurant so that whenever restaurant food is clicked the food is not present there.
        food.setRestaurant(null);
        foodRepository.save(food);
    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId, boolean isVegetarian,
                                         boolean isNonveg, boolean isSeasonal, String foodCategory) throws Exception {

        List<Food> foods= foodRepository.findByRestaurantId(restaurantId);

        if (isVegetarian) {
            foods = filterByVegetarian(foods, isVegetarian);
        }
        if (isNonveg) {
            foods = filterByNonveg(foods, isNonveg);
        }

        if (isSeasonal) {
            foods = filterBySeasonal(foods, isSeasonal);
        }
        //if category is not null and not empty
        if(foodCategory!=null && !foodCategory.equals("")) {
            foods = filterByFoodCategory(foods, foodCategory);
        }
        return foods;
    }

    private List<Food> filterByFoodCategory(List<Food> foods, String foodCategory) {
        return foods.stream()
                .filter(food -> {
                    //f the name of the food category (food.getFoodCategory().getName()) matches the specified foodCategory,
                    // the expression will return true. Otherwise, it will return false.
                    if (food.getFoodCategory() != null) {
                        return food.getFoodCategory().getName().equals(foodCategory);
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
        return foods.stream()
                .filter(food -> food.isSeasonal() == true)
                .collect(Collectors.toList());
    }

    private List<Food> filterByNonveg(List<Food> foods, boolean isNonveg) {
        //Streams are used in Java to process collections of objects for common operations on collections like filtering, mapping.
        //The filter operation is applied to the stream.It takes a lambda expression as an argument.
        return foods.stream()
                .filter(food -> food.isVegetarian() == false)
                .collect(Collectors.toList());
        //It checks each Food object (food) in the stream to see if its isVegetarian property is false
        //After filtering, the collect operation is used to gather the filtered elements into a new list and return it.
    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarian) {
        return foods.stream()
                .filter(food -> food.isVegetarian() ==true)
                .collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if (optionalFood.isEmpty()) {
            throw new Exception("Food not found..");
        }
        return optionalFood.get();
    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) throws Exception {
        Food food = findFoodById(foodId);
        //if available true set it to false, it false set to true (toggle)
        food.setAvailable(!food.isAvailable());
        foodRepository.save(food);
        return food;
    }
}
