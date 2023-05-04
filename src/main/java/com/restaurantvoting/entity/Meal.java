package com.restaurantvoting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "meal")
public class Meal extends AbstractBaseEntity {

    @Column(name = "meal_title")
    private String mealTitle;

    @Column(name = "price")
    private int price;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    public Meal() {
    }

    public Meal(String mealTitle, int price, LocalDate date) {
        this.mealTitle = mealTitle;
        this.price = price;
        this.date = date;
    }

    public String getMealTitle() {
        return mealTitle;
    }

    public void setMealTitle(String mealTitle) {
        this.mealTitle = mealTitle;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id='" + getId() + '\'' +
                ", mealTitle='" + mealTitle + '\'' +
                ", price=" + price + '\'' +
                ", date=" + date.toString() +
                "} ";
    }
}
