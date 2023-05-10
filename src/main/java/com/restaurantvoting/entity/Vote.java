package com.restaurantvoting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
public class Vote extends BaseEntity {

    @Column(name = "user_id", updatable = false)
    private Integer userId;

    @Column(name = "restaurant_id")
    @NotNull
    private Integer restaurantId;

    @Column(name = "date_time")
    private LocalDateTime dateTime = LocalDateTime.now();

    public Vote() {
    }

    public Vote(Integer id, Integer userId, Integer restaurantId, LocalDateTime dateTime) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.dateTime = dateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id='" + getId() + '\'' +
                "userId=" + userId + '\'' +
                ", restaurantId=" + restaurantId + '\'' +
                ", dateTime=" + dateTime.toString() +
                "} ";
    }
}
