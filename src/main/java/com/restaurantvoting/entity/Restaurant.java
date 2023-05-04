package com.restaurantvoting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractBaseEntity{

    @Column(name = "title")
    private String title;

    @Column(name = "active")
    private boolean active;


    public Restaurant() {
    }

    public Restaurant(String title, boolean active) {
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
