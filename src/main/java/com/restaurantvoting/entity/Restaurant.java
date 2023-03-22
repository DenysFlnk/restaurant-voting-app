package com.restaurantvoting.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;


import java.util.List;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractPersistable<Integer> {

    @Column(name = "title")
    private String title;

    @Column(name = "active")
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Meal> currentMenu;

    public Restaurant() {
    }

    public Restaurant(String title, boolean active, List<Meal> currentMenu) {
        this.title = title;
        this.active = active;
        this.currentMenu = currentMenu;
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

    public List<Meal> getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(List<Meal> currentMenu) {
        this.currentMenu = currentMenu;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + getId() + '\'' +
                ", title='" + title + '\'' +
                ", active=" + active + "} ";
    }
}
