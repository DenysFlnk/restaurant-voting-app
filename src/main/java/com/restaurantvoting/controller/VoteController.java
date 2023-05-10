package com.restaurantvoting.controller;

import com.restaurantvoting.entity.Meal;
import com.restaurantvoting.entity.Restaurant;
import com.restaurantvoting.entity.Vote;
import com.restaurantvoting.repository.MealRepository;
import com.restaurantvoting.repository.RestaurantRepository;
import com.restaurantvoting.repository.VoteRepository;
import com.restaurantvoting.to.RestaurantTo;
import com.restaurantvoting.to.VoteTo;
import com.restaurantvoting.util.RestaurantsUtil;
import com.restaurantvoting.util.VotesUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.restaurantvoting.util.DateTimeUtil.*;
import static com.restaurantvoting.util.validation.ValidationUtil.*;

import java.time.LocalDateTime;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Vote vote(@RequestBody @Valid Vote vote){
        log.info("vote {}", vote);
        checkNew(vote);
        assureVotePermission(vote);

        vote.setUserId(1); //TODO add security for getting vote.userId

        Vote oldVote = voteRepository.getVoteByUserIdAndDateTimeBetween(vote.getUserId(), startOfCurrentDay, endOfVoting);
        if (oldVote == null){
            return voteRepository.save(vote);
        }

        vote.setId(oldVote.getId());
        return voteRepository.save(vote);
    }

    @GetMapping("/votes")
    @Cacheable("allVotes_cache")
    public List<VoteTo> getAll(){
        log.info("getAll");
        return VotesUtil.getTos(voteRepository.getAllById(1)); //TODO add security for userId
    }

    @GetMapping("/votes/{voteId}")
    public Vote get(@PathVariable("voteId") int id){
        log.info("get {}", id);
        return checkNotFoundWithId(voteRepository.findById(id).orElse(null), id);
    }

    @PutMapping(value = "/votes/{voteId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Vote vote, @PathVariable("voteId") int id){
        log.info("update {} with id {}", vote, id);
        assureIdConsistent(vote, id);
        LocalDateTime currentDateTime = LocalDateTime.now();
        assureChangeVotePermission(vote, currentDateTime);
        vote.setDateTime(currentDateTime);
        voteRepository.save(vote);
    }

    @DeleteMapping("/votes/{voteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("voteId") int id){
        log.info("delete {}", id);
        Vote deleteVote = checkNotFoundWithId(voteRepository.findById(id).orElse(null), id);
        assureChangeVotePermission(deleteVote, LocalDateTime.now());
        voteRepository.delete(deleteVote);
    }


}
