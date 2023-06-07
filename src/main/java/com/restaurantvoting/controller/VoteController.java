package com.restaurantvoting.controller;

import com.restaurantvoting.entity.Meal;
import com.restaurantvoting.entity.Restaurant;
import com.restaurantvoting.entity.User;
import com.restaurantvoting.entity.Vote;
import com.restaurantvoting.repository.MealRepository;
import com.restaurantvoting.repository.RestaurantRepository;
import com.restaurantvoting.repository.VoteRepository;
import com.restaurantvoting.to.RestaurantTo;
import com.restaurantvoting.to.RestaurantVoteSummaryTo;
import com.restaurantvoting.to.VoteTo;
import com.restaurantvoting.util.RestaurantsUtil;
import com.restaurantvoting.util.VotesUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.time.LocalDateTime;
import java.util.List;

import static com.restaurantvoting.util.DateTimeUtil.endOfVoting;
import static com.restaurantvoting.util.DateTimeUtil.startOfCurrentDay;
import static com.restaurantvoting.util.validation.ValidationUtil.*;

@Tag(name = "Votes", description = "Restaurant choosing, vote management API`s")
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

    @Operation(summary = "Retrieve all possible restaurants for vote")
    @GetMapping("user/restaurants")
    @Cacheable("activeRestaurants_cache")
    public List<RestaurantTo> getAllActiveRestaurants() {
        log.info("getAllActiveRestaurants");

        List<Restaurant> restaurants = restaurantRepository.getAllByActiveTrue();
        if (restaurants == null) return null;

        List<Meal> meals = mealRepository.getAllByCurrentDate();

        return RestaurantsUtil.getRestaurantTos(restaurants, meals);
    }

    @Operation(summary = "Vote for chose restaurant")
    @PostMapping(value = "user/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Vote vote(@RequestBody @Valid Vote vote, @AuthenticationPrincipal(expression = "user") User user) {
        log.info("vote {}", vote);
        checkNew(vote);
        assureVotePermission(vote);

        vote.setUserId(user.id());
        Vote oldVote = voteRepository.getVoteByUserIdAndDateTimeBetween(vote.getUserId(), startOfCurrentDay, endOfVoting);
        if (oldVote != null) {
            vote.setId(oldVote.getId());
            return voteRepository.save(vote);
        }
        return voteRepository.save(vote);
    }

    @Operation(summary = "Retrieve current results of voting")
    @GetMapping("user/votes/results")
    public List<RestaurantVoteSummaryTo> getResultOfVoting() {
        log.info("getResultOfVoting");

        List<Restaurant> restaurants = restaurantRepository.getAllByActiveTrue();
        if (restaurants == null) return null;

        List<Vote> votes = voteRepository.getAllByDateTimeBetween(startOfCurrentDay, endOfVoting);

        return RestaurantsUtil.getRestaurantVoteSummaryTos(restaurants, votes);
    }

    @Operation(summary = "Retrieve all votes belong to logged user")
    @GetMapping("user/votes")
    @Cacheable("allVotes_cache")
    public List<VoteTo> getAll(@AuthenticationPrincipal(expression = "user") User user) {
        log.info("getAll for {}", user.getEmail());
        return VotesUtil.getTos(voteRepository.getAllByUserId(user.id()));
    }

    @Operation(summary = "Retrieve vote by Id")
    @GetMapping("user/votes/{voteId}")
    public Vote get(@PathVariable("voteId") int id, @AuthenticationPrincipal(expression = "user") User user) {
        log.info("get {}", id);
        return checkNotFoundWithId(voteRepository.getByIdAndUserId(id, user.id()).orElse(null), id);
    }

    @Operation(summary = "Modify vote")
    @PutMapping(value = "user/votes/{voteId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Vote vote, @PathVariable("voteId") int id,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                       @AuthenticationPrincipal(expression = "user") User user) {
        log.info("update {} with id {}", vote, id);
        assureIdConsistent(vote, id);
        checkNotFoundWithId(voteRepository.getByIdAndUserId(id, user.id()).orElse(null), id);
        assureChangeVotePermission(vote, dateTime);
        vote.setDateTime(dateTime);
        voteRepository.save(vote);
    }

    @Operation(summary = "Delete vote by Id")
    @DeleteMapping("user/votes/{voteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("voteId") int id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                       @AuthenticationPrincipal(expression = "user") User user) {
        log.info("delete {}", id);
        Vote deleteVote = checkNotFoundWithId(voteRepository.getByIdAndUserId(id, user.id()).orElse(null), id);
        assureChangeVotePermission(deleteVote, dateTime);
        voteRepository.delete(deleteVote);
    }
}
