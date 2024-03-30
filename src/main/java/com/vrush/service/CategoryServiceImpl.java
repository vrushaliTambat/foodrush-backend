package com.vrush.service;

import com.vrush.model.Category;
import com.vrush.model.Restaurant;
import com.vrush.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Category createCategory(String name, Long userId) throws Exception {
        Restaurant restaurant=restaurantService.getRestaurantsByUserId(userId);
        Category category=new Category();
        category.setName(name);
        category.setRestaurant(restaurant);

        return categoryRepository.save(category);

    }

    @Override
    public List<Category> findCategoryByRestaurantId(Long id) throws Exception {
        Restaurant restaurant=restaurantService.getRestaurantsByUserId(id);
        return categoryRepository.findByRestaurantId(id);
    }

    @Override
    public Category findCategoryById(Long id) throws Exception {
        Optional<Category> optionalCategory=categoryRepository.findById(id);
        if(optionalCategory.isEmpty()) {
            throw new Exception("Category does not exist..");
        }
        return optionalCategory.get();
    }
}
