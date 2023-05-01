package com.restaurantvoting.to;

public record MealTo(Integer id, String title, Integer price) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealTo mealTo = (MealTo) o;
        return id.equals(mealTo.id) && title.equals(mealTo.title) && price.equals(mealTo.price);
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}
