package com.restaurantvoting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "meal")
public class Meal extends BaseEntity {

    @Column(name = "meal_title")
    @NotNull
    @NotBlank
    @Size(min = 3, max = 100)
    private String mealTitle;

    @Column(name = "price")
    @NotNull
    @Range(min = 1, max = 5000)
    private int price;

    @Column(name = "date")
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    public Meal() {
    }

    public Meal(Integer id, String mealTitle, int price, LocalDate date, Restaurant restaurant) {
        super(id);
        this.mealTitle = mealTitle;
        this.price = price;
        this.date = date;
        this.restaurant = restaurant;
    }

    public Meal(Meal meal) {
        super(meal.id);
        this.mealTitle = meal.getMealTitle();
        this.price = meal.getPrice();
        this.date = meal.getDate();
        this.restaurant = meal.getRestaurant();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Meal meal = (Meal) o;
        return price == meal.price && mealTitle.equals(meal.mealTitle) && date.equals(meal.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mealTitle, price, date);
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
