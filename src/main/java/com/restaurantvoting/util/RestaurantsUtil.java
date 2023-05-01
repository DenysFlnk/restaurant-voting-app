package com.restaurantvoting.util;

import com.restaurantvoting.entity.Meal;
import com.restaurantvoting.entity.Restaurant;
import com.restaurantvoting.to.RestaurantTo;

import java.util.List;

public class RestaurantsUtil {

    private RestaurantsUtil() {
    }

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants, List<Meal> meals){
        return restaurants.stream().map(restaurant -> createTo(restaurant, meals)).toList();
    }

    public static RestaurantTo createTo(Restaurant restaurant, List<Meal> meals){
        return new RestaurantTo(restaurant.getId(), restaurant.getTitle(), MealsUtil.getTosByRestaurant(meals, restaurant.getId()));
    }
}
