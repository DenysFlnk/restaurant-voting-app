package com.restaurantvoting.controller;

import com.restaurantvoting.entity.Meal;
import com.restaurantvoting.repository.MealRepository;
import com.restaurantvoting.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<Meal> getAll(@PathVariable int id){
        log.info("getAll {}", id);
        return mealRepository.getAllByRestaurantId(id);
    }

    @GetMapping("/{mealId}")
    public Meal get(@PathVariable("id") int id, @PathVariable("mealId") int mealId){
        log.info("get {} for {}", mealId, id);
        return mealRepository.findByIdAndRestaurantId(mealId, id).orElse(null);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Meal create(@RequestBody Meal meal, @PathVariable int id){
        log.info("create {} for {}", meal, id);
        meal.setRestaurant(restaurantRepository.getReferenceById(id));
        return mealRepository.save(meal);
    }

    @DeleteMapping("/{mealId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id, @PathVariable("mealId") int mealId){
        log.info("delete {} for {}", mealId, id);
        mealRepository.findByIdAndRestaurantId(mealId, id).ifPresent(mealRepository::delete);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id){
        log.info("update {} for {}", meal, id);
        meal.setRestaurant(restaurantRepository.getReferenceById(id));
        mealRepository.save(meal);
    }
}
