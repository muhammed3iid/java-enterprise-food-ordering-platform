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
    Order order;
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
    public Response createMenu(@PathParam("username") String username, @PathParam("restaurantName") String restaurantName){
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.role = :role AND u.username =:username", User.class);
        query.setParameter("username", username);
        query.setParameter("role", UserRole.CUSTOMER);
        List<User> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            order = new Order();
            Runner runner = em.find(Runner.class, id);
            em.persist(order);
            return new Response("Customer order successfully created.", user);
        }
        return new Response("Customer username is invalid.", null);
    }
//
//    @POST
//    @Path("/add-meal/{orderID}/{mealName}")
//    public Response addMeal(@PathParam("orderID") int orderID, @PathParam("mealName") String mealName){
//        TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o WHERE o.orderID = :orderID", Order.class);
//        query.setParameter("orderID", orderID);
//        List<Order> resultList = query.getResultList();
//        if (!resultList.isEmpty()){
//            order = resultList.get(0);
//            Meal meal = em.find(Meal.class, mealName);
//            order.getListOfMeals().add(meal);
//            order.setTotalPrice(order.getTotalPrice() + meal.getPrice());
//            em.merge(order);
//            return Response.ok().entity("Order item created successfully.").build();
//        }
//        return Response.ok().entity("Invalid restaurant owner id.").build();
//    }
//
//    @GET
//    @Path("/getorders")
//    public List<Order> getorders() {
//        TypedQuery<Order> o = em.createQuery("SELECT o FROM Order o", Order.class);
//        return o.getResultList();
//    }
//
//    @GET
//    @Path("/getusers")
//    public List<User> getusers() {
//        TypedQuery<User> u = em.createQuery("SELECT p FROM User p", User.class);
//        List<User> users = u.getResultList();
//        return users;
//    }
//
    @GET
    @Path("/getrestaurants")
    public List<Restaurant> getrestaurants() {
        TypedQuery<Restaurant> r = em.createQuery("SELECT r FROM Restaurant r", Restaurant.class);
        return r.getResultList();
    }


}
