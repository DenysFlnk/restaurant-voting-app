package com.restaurantvoting.controller;

import com.restaurantvoting.entity.Restaurant;
import com.restaurantvoting.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "admin/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    private final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public List<Restaurant> getAll(){
        log.info("getAll");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.DESC, "active"));
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id){
        log.info("get {}", id);
        return restaurantRepository.findById(id).orElse(null);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant create(@RequestBody Restaurant restaurant){
        log.info("create {}", restaurant);
        return restaurantRepository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        log.info("delete {}", id);
        restaurantRepository.findById(id).ifPresent(restaurantRepository::delete);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id){
        log.info("update {} with id {}", restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable int id, @RequestParam boolean active){
        log.info(active ? "activate {}" : "deactivate {}", id);
        Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
        restaurant.setActive(active);
        restaurantRepository.save(restaurant);
    }
}
