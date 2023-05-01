package com.restaurantvoting.to;



import java.util.List;
import java.util.Objects;

public record RestaurantTo (Integer id, String title, List<MealTo> menu){
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTo that = (RestaurantTo) o;
        return id.equals(that.id) && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", title='" + title + '\'' + '}';
    }
}
