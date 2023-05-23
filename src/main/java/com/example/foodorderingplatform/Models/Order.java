package com.example.foodorderingplatform.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int orderID;
    double totalPrice;
    String status; // preparing delivered canceled
//    @OneToMany(mappedBy = "order")
//    List<Meal> listOfMeals;
//    @ManyToOne
//    @JoinColumn(name = "restaurantID")
//    @JsonBackReference
//    Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "runnerID")
    Runner runner;

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

//    public List<Meal> getListOfMeals() {
//        return listOfMeals;
//    }
//
//    public void setListOfMeals(List<Meal> listOfMeals) {
//        this.listOfMeals = listOfMeals;
//    }

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
