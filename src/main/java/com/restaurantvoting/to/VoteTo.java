package com.restaurantvoting.to;

import java.time.LocalDateTime;
import java.util.Objects;

public record VoteTo(Integer id, Integer userId, Integer restaurantId, LocalDateTime dateTime, boolean editable) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteTo voteTo = (VoteTo) o;
        return editable == voteTo.editable && Objects.equals(id, voteTo.id) && Objects.equals(userId, voteTo.userId)
                && Objects.equals(restaurantId, voteTo.restaurantId) && Objects.equals(dateTime, voteTo.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, restaurantId, dateTime, editable);
    }
}
