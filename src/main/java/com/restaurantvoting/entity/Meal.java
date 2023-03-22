package com.restaurantvoting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "meal")
public class Meal extends AbstractPersistable<Integer> {

    @Column(name = "meal_title")
    private String mealTitle;

    @Column(name = "price")
    private int price;

    public Meal() {
    }

    public Meal(String mealTitle, int price) {
        this.mealTitle = mealTitle;
        this.price = price;
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

    @Override
    public String toString() {
        return "Meal{" +
                "id='" + getId() + '\'' +
                ", mealTitle='" + mealTitle + '\'' +
                ", price=" + price +
                "} ";
    }
}
