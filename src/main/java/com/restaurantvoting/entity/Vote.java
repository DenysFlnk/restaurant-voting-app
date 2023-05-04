package com.restaurantvoting.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
public class Vote extends AbstractBaseEntity{

    @Column(name = "user_id", insertable = false, updatable = false)
    private int userId;

    @Column(name = "restaurant_id", insertable = false)
    private int restaurantId;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public Vote() {
    }

    public Vote(int userId, int restaurantId, LocalDateTime dateTime) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.dateTime = dateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
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
