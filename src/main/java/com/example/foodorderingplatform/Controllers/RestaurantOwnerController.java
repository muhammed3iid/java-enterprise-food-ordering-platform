package com.example.foodorderingplatform.Controllers;

import com.example.foodorderingplatform.Models.Meal;
import com.example.foodorderingplatform.Models.Restaurant;
import com.example.foodorderingplatform.Models.User;
import com.example.foodorderingplatform.Models.UserRole;
import com.example.foodorderingplatform.Utils.Response;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.List;

@Stateless
@Path("/restaurant-owner")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantOwnerController {

    User user;
    Restaurant restaurant;
    Meal meal;
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
        user.setRole(UserRole.OWNER);
        em.persist(user);
        return new Response("Restaurant owner account successfully created.", user);
    }

    @GET
    @Path("/login/{username}/{password}")
    public Response login(@PathParam("username") String username, @PathParam("password") String password) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<User> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            user = resultList.get(0);
            return new Response("Restaurant owner successfully logged in.", user);
        }
        return new Response("Restaurant owner account not found", null);
    }

    @POST
    @Path("/create-menu/{ownerID}/{restaurantName}")
    public Response createMenu(@PathParam("ownerID") int ownerID, @PathParam("restaurantName") String restaurantName){
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.role = :role AND u.userID =:ownerID", User.class);
        query.setParameter("ownerID", ownerID);
        query.setParameter("role", UserRole.OWNER);
        List<User> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            restaurant = new Restaurant();
            restaurant.setOwnerID(resultList.get(0).getUserID());
            restaurant.setName(restaurantName);
            em.persist(restaurant);
            return new Response("Restaurant menu successfully created.", restaurant);
        }
        return new Response("Restaurant owner ID is invalid.", null);
    }

    @POST
    @Path("/add-meal/{restaurantID}/{mealName}/{price}")
    public Response addMeal(@PathParam("restaurantID") int restaurantID, @PathParam("mealName") String mealName, @PathParam("price") double price){
        TypedQuery<Restaurant> query = em.createQuery("SELECT r FROM Restaurant r WHERE r.restaurantID = :restaurantID", Restaurant.class);
        query.setParameter("restaurantID", restaurantID);
        List<Restaurant> resultList = query.getResultList();
        if (!resultList.isEmpty()){
            restaurant = resultList.get(0);
            meal = new Meal();
            meal.setName(mealName);
            meal.setPrice(price);
            meal.setRestaurant(restaurant);
            restaurant.getListOfMeals().add(meal);
            em.persist(meal);
            em.merge(restaurant);
            return new Response("Meal successfully added.", restaurant);
        }
        return new Response("Restaurant ID is invalid.", null);
    }

    @GET
    @Path("/get-restaurant/{id}")
    public Restaurant getRestaurant(@PathParam("id") int id) {
        return em.find(Restaurant.class, id);
    }
}
