package com.restaurantvoting.util;

import com.restaurantvoting.entity.Meal;
import com.restaurantvoting.to.MealTo;

import java.util.List;

public class MealsUtil {

    private MealsUtil(){

    }

    public static List<MealTo> getTos(List<Meal> meals){
        return meals.stream().map(MealsUtil::createTo).toList();
    }

    public static List<MealTo> getTosByRestaurant(List<Meal> meals, int restaurantId){
        return meals.stream().filter(meal -> meal.getRestaurant().getId() == restaurantId).map(MealsUtil::createTo).toList();
    }

    public static MealTo createTo(Meal meal){
        return new MealTo(meal.getId(), meal.getMealTitle(), meal.getPrice());
    }
}
