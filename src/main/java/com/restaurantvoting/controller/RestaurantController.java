package com.restaurantvoting.controller;

import com.restaurantvoting.entity.Restaurant;
import com.restaurantvoting.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.restaurantvoting.util.validation.ValidationUtil.*;

import java.util.List;

@Tag(name = "Restaurants", description = "Restaurant management API`s")
@RestController
@RequestMapping(value = "admin/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    private final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Operation(summary = "Retrieve all restaurants")
    @GetMapping
    public List<Restaurant> getAll(){
        log.info("getAll");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.DESC, "active"));
    }

    @Operation(summary = "Retrieve restaurant by Id")
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id){
        log.info("get {}", id);
        return checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
    }

    @Operation(summary = "Create new restaurant")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurant create(@RequestBody @Valid Restaurant restaurant){
        log.info("create {}", restaurant);
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

    @Operation(summary = "Delete restaurant by Id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        log.info("delete {}", id);
        Restaurant deleteRestaurant = checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
        restaurantRepository.delete(deleteRestaurant);
    }

    @Operation(summary = "Modify restaurant")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Restaurant restaurant, @PathVariable int id){
        log.info("update {} with id {}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @Operation(summary = "Activate or deactivate restaurant")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable int id, @RequestParam boolean active){
        log.info(active ? "activate {}" : "deactivate {}", id);
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
        restaurant.setActive(active);
        restaurantRepository.save(restaurant);
    }
}
