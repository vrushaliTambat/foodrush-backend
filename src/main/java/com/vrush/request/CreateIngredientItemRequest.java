package com.vrush.request;

import lombok.Data;

@Data
public class CreateIngredientItemRequest {
   private Long restaurantId;
   private String name;
   private Long categoryId;
}
