package com.restaurantvoting.to;



import java.util.List;

public record RestaurantTo (Integer id, String title, List<MealTo> menu){
}
