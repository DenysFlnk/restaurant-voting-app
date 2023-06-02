package com.restaurantvoting.util;

import com.restaurantvoting.entity.Meal;
import com.restaurantvoting.entity.Restaurant;
import com.restaurantvoting.entity.Vote;
import com.restaurantvoting.to.RestaurantTo;
import com.restaurantvoting.to.RestaurantVoteSummaryTo;

import java.util.Comparator;
import java.util.List;

public class RestaurantsUtil {

    private RestaurantsUtil() {
    }

    public static List<RestaurantTo> getRestaurantTos(List<Restaurant> restaurants, List<Meal> meals){
        return restaurants.stream().map(restaurant -> createRestaurantTo(restaurant, meals)).toList();
    }

    private static RestaurantTo createRestaurantTo(Restaurant restaurant, List<Meal> meals){
        return new RestaurantTo(restaurant.getId(), restaurant.getTitle(), MealsUtil.getTosByRestaurant(meals, restaurant.id()));
    }

    public static List<RestaurantVoteSummaryTo> getRestaurantVoteSummaryTos(List<Restaurant> restaurants, List<Vote> votes) {
        return restaurants.stream().map(restaurant -> createRestaurantVoteSummaryTo(restaurant, votes))
                .sorted(Comparator.comparingInt(RestaurantVoteSummaryTo::voteCount).reversed())
                .toList();
    }

    private static RestaurantVoteSummaryTo createRestaurantVoteSummaryTo(Restaurant restaurant, List<Vote> votes) {
        Integer voteCount = votes.stream().filter(vote -> vote.getRestaurantId() == restaurant.id()).mapToInt(e -> 1).sum();
        return new RestaurantVoteSummaryTo(restaurant.id(), restaurant.getTitle(), voteCount);
    }
}
