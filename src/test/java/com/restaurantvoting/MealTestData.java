package com.restaurantvoting;

import com.restaurantvoting.entity.Meal;

import java.time.LocalDate;
import java.util.List;

import static com.restaurantvoting.RestaurantTestData.*;

public class MealTestData {

    private static final String MEAL_CONTROLLER_URI = "/admin/restaurants/%s/meals";

    public static final Meal tea = new Meal(1, "Tea", 2, LocalDate.now(), bistro);

    public static final Meal coffee = new Meal(2, "Coffee", 3, LocalDate.now(), cafeteria);

    public static final Meal vine = new Meal(3, "Vine", 5, LocalDate.now(), restaurant);

    public static final Meal salad = new Meal(4, "Salad", 5, LocalDate.now(), bistro);

    public static final Meal croissant = new Meal(5, "Croissant", 4, LocalDate.now(), cafeteria);

    public static final Meal steak = new Meal(6, "Steak", 8, LocalDate.now(), restaurant);

    public static final List<Meal> allTodayMeals = List.of(tea, coffee, vine, salad, croissant, steak);

    public static final List<Meal> allRestaurantMeals = List.of(vine, steak);

    public static Meal getNew() {
        return new Meal(null, "Sushi", 10, LocalDate.now(), restaurant);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(salad);

        updated.setPrice(4);
        updated.setMealTitle("Discounted salad");

        return updated;
    }

    public static String getMealControllerUri(int restaurantId) {
        return String.format(MEAL_CONTROLLER_URI, restaurantId);
    }
}
