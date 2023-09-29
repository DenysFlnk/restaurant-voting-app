package com.restaurantvoting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "vote")
public class Vote extends BaseEntity {

    @Column(name = "user_id", updatable = false)
    private Integer userId;

    @Column(name = "restaurant_id")
    @NotNull
    private Integer restaurantId;

    @Column(name = "date_time")
    @NotNull
    private LocalDateTime dateTime;

    public Vote() {
    }

    public Vote(Integer id, Integer userId, Integer restaurantId, LocalDateTime dateTime) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.dateTime = dateTime;
    }

    public Vote(Vote vote) {
        super(vote.id);
        this.userId = vote.userId;
        this.restaurantId = vote.restaurantId;
        this.dateTime = vote.dateTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRestaurantId() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Vote vote = (Vote) o;
        return userId.equals(vote.userId) && restaurantId.equals(vote.restaurantId) && dateTime.equals(vote.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, restaurantId, dateTime);
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
