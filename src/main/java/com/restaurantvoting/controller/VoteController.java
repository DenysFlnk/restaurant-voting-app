package com.restaurantvoting.controller;

import com.restaurantvoting.entity.Meal;
import com.restaurantvoting.entity.Restaurant;
import com.restaurantvoting.entity.Vote;
import com.restaurantvoting.repository.MealRepository;
import com.restaurantvoting.repository.RestaurantRepository;
import com.restaurantvoting.repository.VoteRepository;
import com.restaurantvoting.to.RestaurantTo;
import com.restaurantvoting.util.RestaurantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.restaurantvoting.util.DateTimeUtil.*;

import java.util.List;

@RestController
@RequestMapping(value = "user/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    private final Logger log = LoggerFactory.getLogger(VoteController.class);

    private final RestaurantRepository restaurantRepository;

    private final VoteRepository voteRepository;

    private final MealRepository mealRepository;

    @Autowired
    public VoteController(RestaurantRepository restaurantRepository, VoteRepository voteRepository, MealRepository mealRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.mealRepository = mealRepository;
    }

    @GetMapping
    @Cacheable("activeRestaurants_cache")
    public List<RestaurantTo> getAllActiveRestaurants(){
        log.info("getAllActiveRestaurants");

        List<Restaurant> restaurants = restaurantRepository.getAllByActiveTrue();
        if (restaurants == null) return null;

        List<Meal> meals = mealRepository.getAllByCurrentDate();

        return RestaurantsUtil.getTos(restaurants, meals);
    }

    @GetMapping("/{id}/votes")
    public int getCurrentDayVotesCountByRestaurant(@PathVariable int id){
        log.info("getCurrentDayVotesCountByRestaurant {}", id);
        return voteRepository.countAllByRestaurantIdAndDateTimeBetween(id, startOfCurrentDay, endOfVoting);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Vote vote(@RequestBody Vote vote){
        log.info("vote {}", vote);

        //TODO Validation: time before 11:00

        vote.setUserId(1); //TODO add security for getting vote.userId

        Vote oldVote = voteRepository.getVoteByUserIdAndDateTimeBetween(vote.getUserId(), startOfCurrentDay, endOfVoting);

        if (oldVote == null){
            return voteRepository.save(vote);
        }

        vote.setId(oldVote.getId());
        return voteRepository.save(vote);
    }
}
