package com.vrush.service;

import com.vrush.model.IngredientCategory;
import com.vrush.model.IngredientsItem;
import com.vrush.model.Restaurant;
import com.vrush.repository.IngredientCategoryRepository;
import com.vrush.repository.IngredientItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientsService {
    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepo;

    @Autowired
    private IngredientItemRepository ingredientItemRepo;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public IngredientCategory createIngredientsCategory(String name, Long restaurantId) throws Exception {
        Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
        IngredientCategory category= new IngredientCategory();
        category.setRestaurant(restaurant);
        category.setName(name);
        return ingredientCategoryRepo.save(category);

    }

    @Override
    public IngredientCategory findIngredientsCategoryById(Long id) throws Exception {
        Optional<IngredientCategory> opt=ingredientCategoryRepo.findById(id);
        if(opt.isEmpty()){
            throw new Exception("Ingredient category not found");
        }
        return opt.get();
    }

    @Override
    public List<IngredientCategory> findIngredientsCategoryByRestaurantId(Long id) throws Exception {
        //findRestaurantById method laready throws exception so we don't need to throw it here.
        restaurantService.findRestaurantById(id);
        return ingredientCategoryRepo.findByRestaurantId(id);
    }

    @Override
    public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId) {
        return ingredientItemRepo.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem createIngredientItem(Long restaurantId,
                                                String ingredientName, Long categoryId) throws Exception {
        Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
        IngredientCategory category = findIngredientsCategoryById(categoryId);

        IngredientsItem item=new IngredientsItem();
        item.setName(ingredientName);
        item.setRestaurant(restaurant);
        item.setCategory(category);

        IngredientsItem ingredientsItem = ingredientItemRepo.save(item);
        category.getIngredients().add(item);

        return ingredientsItem;
    }

    @Override
    public IngredientsItem updateStock(Long id) throws Exception {
        Optional<IngredientsItem> opt=ingredientItemRepo.findById(id);
        if(opt.isEmpty()){
            throw new Exception("Ingredient not found");
        }
        IngredientsItem ingredientsItem = opt.get();
        ingredientsItem.setInStock(!ingredientsItem.isInStock());
        return ingredientItemRepo.save(ingredientsItem);
    }
}
