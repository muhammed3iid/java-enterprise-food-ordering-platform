package com.example.foodorderingplatform.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OrderBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int orderID;
    double totalPrice;
    String status; // preparing delivered canceled
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<Meal> listOfMeals;
//    @ManyToOne
//    @JoinColumn(name = "restaurantID")
//    @JsonBackReference
//    Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "runnerID")
    Runner runner;

    public OrderBean(){
        listOfMeals = new ArrayList<>();
    }
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Meal> getListOfMeals() {
        return listOfMeals;
    }

    public void setListOfMeals(List<Meal> listOfMeals) {
        this.listOfMeals = listOfMeals;
    }

//    public Restaurant getRestaurant() {
//        return restaurant;
//    }
//
//    public void setRestaurant(Restaurant restaurant) {
//        this.restaurant = restaurant;
//    }

    public Runner getRunner() {
        return runner;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }
}
