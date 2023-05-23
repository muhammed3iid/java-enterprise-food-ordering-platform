package com.example.foodorderingplatform.Models;

import javax.persistence.*;
import java.util.List;


@Entity
public class Runner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int runnerID;
    String name;
    boolean status; // available busy
    double deliveryFees;
    @OneToMany(mappedBy = "runner")
    List<OrderBean> listOfOrders;

    public int getRunnerID() {
        return runnerID;
    }

    public void setRunnerID(int runnerID) {
        this.runnerID = runnerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getDeliveryFees() {
        return deliveryFees;
    }

    public void setDeliveryFees(double deliveryFees) {
        this.deliveryFees = deliveryFees;
    }
}
