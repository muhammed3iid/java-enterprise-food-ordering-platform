package com.example.foodorderingplatform.Controllers;

import com.example.foodorderingplatform.Models.*;
import com.example.foodorderingplatform.Utils.Response;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("/customer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerController {

    User user;
    OrderBean orderObj;
    @PersistenceContext
    private EntityManager em;

    @POST
    @Path("/signup/{name}/{username}/{password}")
    public Response signup(@PathParam("name") String name, @PathParam("username") String username, @PathParam("password") String password) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class);
        query.setParameter("username", username);
        Long count = query.getSingleResult();
        if (count > 0) {
            return new Response("Username already exists.", null);
        }
        user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(UserRole.CUSTOMER);
        em.persist(user);
        return new Response("Customer account successfully created.", user);
    }

    @GET
    @Path("/login/{username}/{password}")
    public Response login(@PathParam("username") String username, @PathParam("password") String password) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<User> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            User user = resultList.get(0);
            if(user.getPassword().equals(password)){
                return new Response("Customer successfully logged in.", user);
            }
        }
        return new Response("Customer account not found", null);
    }

    @POST
    @Path("/create-order/{username}/{restaurantName}")
    public Response createOrder(@PathParam("username") String username, @PathParam("restaurantName") String restaurantName){
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.role = :role AND u.username =:username", User.class);
        query.setParameter("username", username);
        query.setParameter("role", UserRole.CUSTOMER);
        List<User> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            orderObj = new OrderBean();
            TypedQuery<Runner> q = em.createQuery("SELECT r FROM Runner r WHERE r.status = :status", Runner.class);
            q.setParameter("status", true);
            List<Runner> reslist = q.getResultList();
            if(!reslist.isEmpty()){
                Runner runner = reslist.get(0);
                orderObj.setRunner(runner);
                orderObj.setTotalPrice(0.0 + runner.getDeliveryFees());
                orderObj.setStatus("Preparing");
                runner.setStatus(false);
                em.merge(runner);
                em.persist(orderObj);
                return new Response("Customer order successfully created.", orderObj);
            }
        }
        return new Response("Customer username is invalid.", null);
    }

    @POST
    @Path("/add-meal/{orderID}/{mealName}")
    public Response addMeal(@PathParam("orderID") int orderID, @PathParam("mealName") String mealName){
        TypedQuery<OrderBean> query = em.createQuery("SELECT o FROM OrderBean o WHERE o.orderID = :orderID", OrderBean.class);
        query.setParameter("orderID", orderID);
        List<OrderBean> resultList = query.getResultList();
        if (!resultList.isEmpty()){
            orderObj = resultList.get(0);
            TypedQuery<Meal> q = em.createQuery("SELECT m FROM Meal m WHERE m.name = :mealName", Meal.class);
            q.setParameter("mealName", mealName);
            List<Meal> reslist = q.getResultList();
            if(!reslist.isEmpty()){
                Meal meal = reslist.get(0);
                orderObj.setStatus("Preparing");
                orderObj.getListOfMeals().add(meal);
                orderObj.setTotalPrice(orderObj.getTotalPrice() + meal.getPrice());
                em.merge(orderObj);
                return new Response("Customer meal successfully added to order.", orderObj);
            }
        }
        return new Response("Order ID or meal name is invalid.", null);
    }

    @GET
    @Path("/get-restaurants")
    public List<Restaurant> getrestaurants() {
        TypedQuery<Restaurant> r = em.createQuery("SELECT r FROM Restaurant r", Restaurant.class);
        return r.getResultList();
    }


}
