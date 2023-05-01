package com.restaurantvoting.repository;

import com.restaurantvoting.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MealRepository extends JpaRepository<Meal, Integer> {

    @Query("SELECT m FROM Meal m WHERE m.date = CURRENT DATE")
    List<Meal> getAllByCurrentDate();
}
