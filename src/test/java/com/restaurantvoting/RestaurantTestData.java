package com.restaurantvoting;

import com.restaurantvoting.entity.Restaurant;
import com.restaurantvoting.to.RestaurantTo;
import com.restaurantvoting.to.RestaurantVoteSummaryTo;
import com.restaurantvoting.util.RestaurantsUtil;

import java.util.List;

public class RestaurantTestData {

    public static final String RESTAURANT_CONTROLLER_URI = "/admin/restaurants";

    public static final Restaurant bistro = new Restaurant(1, "Bistro", true);

    public static final Restaurant cafeteria = new Restaurant(2, "Cafeteria", true);

    public static final Restaurant restaurant = new Restaurant(3, "Restaurant", true);

    public static final Restaurant inactiveRestaurant = new Restaurant(4, "Inactive restaurant", false);

    public static final List<Restaurant> allRestaurants = List.of(bistro, cafeteria, restaurant, inactiveRestaurant);

    private static final List<Restaurant> activeRestaurants = List.of(bistro, cafeteria, restaurant);

    public static final List<RestaurantTo> activeRestaurantTos = RestaurantsUtil.getRestaurantTos(activeRestaurants, MealTestData.allTodayMeals);

    public static final List<RestaurantVoteSummaryTo> restaurantVoteSummaryTos = RestaurantsUtil.getRestaurantVoteSummaryTos(activeRestaurants, VoteTestData.allCurrentDayVotes);

    public static Restaurant getNew() {
        return new Restaurant(null, "New restaurant", false);
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(cafeteria);

        updated.setTitle("Cafeteria updated ");

        return updated;
    }


    public static Restaurant getDeactivated() {
        Restaurant deactivated = new Restaurant(bistro);

        deactivated.setActive(false);

        return deactivated;
    }

}
