package com.restaurantvoting.to;

import java.time.LocalDateTime;

public record VoteTo(Integer id, Integer userId, Integer restaurantId, LocalDateTime dateTime, boolean editable) {
}
