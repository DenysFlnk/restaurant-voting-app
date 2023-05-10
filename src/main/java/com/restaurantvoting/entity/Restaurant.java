package com.restaurantvoting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "restaurant")
public class Restaurant extends BaseEntity {

    @Column(name = "title")
    @NotNull
    @NotBlank
    @Size(min = 3, max = 30)
    private String title;

    @Column(name = "active")
    @NotNull
    private boolean active;


    public Restaurant() {
    }

    public Restaurant(Integer id, String title, boolean active) {
        super(id);
        this.title = title;
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + getId() + '\'' +
                ", title='" + title + '\'' +
                ", active=" + active + "} ";
    }
}
