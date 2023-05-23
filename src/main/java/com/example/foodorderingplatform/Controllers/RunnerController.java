package com.example.foodorderingplatform.Controllers;

import com.example.foodorderingplatform.Models.Runner;
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
@Path("/runner")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RunnerController {
    User user;
    Runner runner;
    @PersistenceContext
    private EntityManager em;

    @POST
    @Path("/signup/{name}/{username}/{password}/{fees}")
    public Response signup(@PathParam("name") String name, @PathParam("username") String username, @PathParam("password") String password, @PathParam("fees") double fees) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class);
        query.setParameter("username", username);
        Long count = query.getSingleResult();
        if (count > 0) {
            return new Response("Username already exists.", null);
        }
        user = new User();
        runner = new Runner();
        user.setName(name);
        runner.setName(name);
        user.setUsername(username);
        runner.setStatus(true);
        user.setPassword(password);
        runner.setDeliveryFees(fees);
        user.setRole(UserRole.RUNNER);
        em.persist(user);
        em.persist(runner);
        return new Response("Runner account successfully created.", runner);
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
                return new Response("Runner successfully logged in.", user);
            }
        }
        return new Response("Runner account not found", null);
    }

    @PUT
    @Path("/change-status/{runnerID}")
    public Response changeStatus(@PathParam("runnerID") int runnerID) {
        Runner runner = em.find(Runner.class, runnerID);
        if(runner.isStatus()){
            runner.setStatus(false);
        }
        else{
            runner.setStatus(true);
        }
        em.merge(runner);
        return new Response("Runner status successfully changed", runner);
    }
}
