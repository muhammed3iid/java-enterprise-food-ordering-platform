package com.example.foodorderingplatform.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int restaurantID;
    String name;
    int ownerID;
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<Meal> listOfMeals;
//    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonManagedReference
//    List<Order> listOfOrders;

    public Restaurant() {
        listOfMeals = new ArrayList<>();
//        listOfOrders = new ArrayList<>();
    }
    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public List<Meal> getListOfMeals() {
        return listOfMeals;
    }

    public void setListOfMeals(List<Meal> listOfMeals) {
        this.listOfMeals = listOfMeals;
    }

//    public List<Order> getListOfOrders() {
//        return listOfOrders;
//    }
//
//    public void setListOfOrders(List<Order> listOfOrders) {
//        this.listOfOrders = listOfOrders;
//    }
}
