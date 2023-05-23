package com.restaurantvoting.controller;

import com.restaurantvoting.entity.Meal;
import com.restaurantvoting.entity.Restaurant;
import com.restaurantvoting.entity.User;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.restaurantvoting.util.DateTimeUtil.*;
import static com.restaurantvoting.util.validation.ValidationUtil.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("user/restaurants")
    @Cacheable("activeRestaurants_cache")
    public List<RestaurantTo> getAllActiveRestaurants(){
        log.info("getAllActiveRestaurants");

        List<Restaurant> restaurants = restaurantRepository.getAllByActiveTrue();
        if (restaurants == null) return null;

        List<Meal> meals = mealRepository.getAllByCurrentDate();

        return RestaurantsUtil.getTos(restaurants, meals);
    }

    @PostMapping(value = "user/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Vote vote(@RequestBody @Valid Vote vote, @AuthenticationPrincipal(expression = "user") User user){
        log.info("vote {}", vote);
        checkNew(vote);
        assureVotePermission(vote);

        vote.setUserId(user.id());
        Vote oldVote = voteRepository.getVoteByUserIdAndDateTimeBetween(vote.getUserId(), startOfCurrentDay, endOfVoting);
        if (oldVote != null){
            vote.setId(oldVote.getId());
            return voteRepository.save(vote);
        }
        return voteRepository.save(vote);
    }

    @GetMapping("user/votes")
    @Cacheable("allVotes_cache")
    public List<VoteTo> getAll(@AuthenticationPrincipal(expression = "user") User user){
        log.info("getAll for {}", user.getEmail());
        return VotesUtil.getTos(voteRepository.getAllById(user.id()));
    }

    @GetMapping("user/votes/{voteId}")
    public Vote get(@PathVariable("voteId") int id, @AuthenticationPrincipal(expression = "user") User user){
        log.info("get {}", id);
        Vote vote = checkNotFoundWithId(voteRepository.findById(id).orElse(null), id);
        checkVoteBelongsUser(vote, user.id());
        return vote;
    }

    @PutMapping(value = "user/votes/{voteId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Vote vote, @PathVariable("voteId") int id,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                       @AuthenticationPrincipal(expression = "user") User user){
        log.info("update {} with id {}", vote, id);
        assureIdConsistent(vote, id);
        checkVoteBelongsUser(vote, user.id());
        assureChangeVotePermission(vote, dateTime);
        vote.setDateTime(dateTime);
        voteRepository.save(vote);
    }

    @DeleteMapping("user/votes/{voteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("voteId") int id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                       @AuthenticationPrincipal(expression = "user") User user){
        log.info("delete {}", id);
        Vote deleteVote = checkNotFoundWithId(voteRepository.findById(id).orElse(null), id);
        checkVoteBelongsUser(deleteVote, user.id());
        assureChangeVotePermission(deleteVote, dateTime);
        voteRepository.delete(deleteVote);
    }

}
