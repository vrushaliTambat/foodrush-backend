package com.vrush.service;

import com.vrush.model.IngredientCategory;
import com.vrush.model.IngredientsItem;

import java.util.List;

public interface IngredientsService {
    public IngredientCategory createIngredientsCategory(String name,Long restaurantId) throws Exception;

    public IngredientCategory findIngredientsCategoryById(Long id) throws Exception;

    public List<IngredientCategory> findIngredientsCategoryByRestaurantId(Long id) throws Exception;

    public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId);

    public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName,
                                                 Long categoryId) throws Exception;
    public IngredientsItem updateStock(Long id) throws Exception;
}
