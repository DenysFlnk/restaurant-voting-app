package com.restaurantvoting.controller;

import com.restaurantvoting.entity.Meal;
import com.restaurantvoting.repository.MealRepository;
import com.restaurantvoting.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.restaurantvoting.util.validation.ValidationUtil.*;

import java.util.List;

@Tag(name = "Meals", description = "Meal management API`s")
@RestController
@RequestMapping("admin/restaurants/{id}/meals")
public class MealController {

    private final Logger log = LoggerFactory.getLogger(MealController.class);

    private final MealRepository mealRepository;

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public MealController(MealRepository mealRepository, RestaurantRepository restaurantRepository) {
        this.mealRepository = mealRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Operation(summary = "Retrieve all meals belong to restaurant")
    @GetMapping
    public List<Meal> getAll(@PathVariable int id){
        log.info("getAll {}", id);
        return mealRepository.getAllByRestaurantId(id);
    }

    @Operation(summary = "Retrieve meal by restaurant Id and meal Id")
    @GetMapping("/{mealId}")
    public Meal get(@PathVariable("id") int id, @PathVariable("mealId") int mealId){
        log.info("get {} for restaurant {}", mealId, id);
        return checkNotFoundWithId(mealRepository.getMealByIdAndRestaurantId(mealId, id).orElse(null), mealId);
    }

    @Operation(summary = "Create new meal for restaurant")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Meal create(@RequestBody @Valid Meal meal, @PathVariable int id){
        log.info("create {} for restaurant {}", meal, id);
        checkNew(meal);
        meal.setRestaurant(restaurantRepository.getReferenceById(id));
        return mealRepository.save(meal);
    }

    @Operation(summary = "Delete meal by restaurant Id and meal Id")
    @DeleteMapping("/{mealId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id, @PathVariable("mealId") int mealId){
        log.info("delete with id={} for restaurant {}", mealId, id);
        Meal deleteMeal = checkNotFoundWithId(mealRepository.getMealByIdAndRestaurantId(mealId, id).orElse(null), mealId);
        mealRepository.delete(deleteMeal);
    }

    @Operation(summary = "Modify meal")
    @PutMapping("/{mealId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Meal meal, @PathVariable("id") int id, @PathVariable("mealId") int mealId){
        log.info("update {} with id={} for restaurant {}", meal, mealId, id);
        assureIdConsistent(meal, mealId);
        Meal toUpdate = checkNotFoundWithId(mealRepository.getMealByIdAndRestaurantId(mealId, id).orElse(null), mealId);
        meal.setRestaurant(toUpdate.getRestaurant());
        mealRepository.save(meal);
    }
}
