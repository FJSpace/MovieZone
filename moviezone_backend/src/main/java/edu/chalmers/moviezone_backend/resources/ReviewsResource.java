/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend.resources;

import edu.chalmers.moviezone_backend.MovieZone;
import edu.chalmers.moviezone_backend.Review;
import edu.chalmers.moviezone_backend.crypto.Tokens;
import edu.chalmers.moviezone_backend.wrappers.ReviewWrapper;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.jose4j.lang.JoseException;

import com.google.gson.Gson;

/**
 *
 * @author Space
 */
@Stateless
@LocalBean
@Path("reviews")
public class ReviewsResource {
    
     @PersistenceContext(unitName = "moviezone_pu",type=PersistenceContextType.TRANSACTION)
    private EntityManager em;
   
    @Context
    private UriInfo uriInfo;
    
    @Inject
    private MovieZone mz;
    
    @GET
    @Path("movie/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getReviewsByMovie(@PathParam("id") String movieId){
        Gson gs = new Gson();
        List<Review> reviews = mz.getReviews().getByMovie(movieId);
        return Response.ok(gs.toJson(reviews)).build();
    }
    
    @GET
    @Path("user/{username}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getReviewsByUser(@PathParam("username") String username){ 
        Gson gs = new Gson();
        Long id = new Long(0);
        List<Review> reviews = mz.getReviews().getByEnjoyer(id);
        return Response.ok(gs.toJson(reviews)).build();
    }
    
    @GET
    @Path("latest/{nr}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getLatestReviews(@PathParam("nr") int nrOfRev){
        Gson gs = new Gson();
        List<Review> rs = mz.getReviews().getLatestReviews(nrOfRev);
        return Response.ok(gs.toJson(rs)).build();
    }
    
    @GET
    @Path("latest/user/{nr}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getLatestByEnjoyer(@HeaderParam("authorization") String token,
                                     @PathParam("nr") int nrOfRev){
        Gson gs = new Gson();
        try{
            Long id = Tokens.fromToken(token);
            List<Review> rs = mz.getReviews().getLatestByEnjoyer(id, nrOfRev);
            return Response.ok(gs.toJson(rs)).build();
        } catch(JoseException je) { return Response.status(401).build(); }
    }
    
    @GET
    @Path("me/{movieId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMyReview(@HeaderParam("authorization") String token,
                                @PathParam("movieId") String movieId) {
        Gson gs = new Gson();
        try{
            Long userId = Tokens.fromToken(token);
            String jpql = "SELECT r FROM Review r WHERE r.userId=:userId AND r.movieId=:movieId";
            Review r = em.createQuery(jpql, Review.class)
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .getSingleResult();
            return Response.ok(gs.toJson(r)).build();
        }catch(JoseException je) { return Response.status(401).build(); }
        catch(NoResultException nre){ return Response.status(417).build(); }
    }
    
    @POST
    @Path("{movieId}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response postReview(@HeaderParam("authorization") String token,
                               @PathParam("movieId") String movieId,
                               JsonObject jo) { 
        try{
            Long id = Tokens.fromToken(token);
            String user = mz.getEnjoyers().getUserById(id);
            Review rev = new Review(movieId,id,user,jo.getString("review"));
            mz.getReviews().create(rev);
            URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(rev.getId())).build(rev);
            return Response.created(uri).build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }
    
    @PUT
    @Path("{movieId}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response editReview(@HeaderParam("authorization") String token,
                               @PathParam("movieId") String movieId,
                               JsonObject jo) { 
        try{
            Long userId = Tokens.fromToken(token);
            String jpql = "SELECT r FROM Review r WHERE r.userId=:id AND r.movieId=:movieId";
            Review r = em.createQuery(jpql, Review.class)
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .getSingleResult();
            if(r != null){
                Review rev = new Review(r.getId(),movieId,userId,r.getUserName(), jo.getString("review"));
                mz.getReviews().update(rev);
                return Response.noContent().build();
            }
            else
                return Response.status(400).build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }
    
    @DELETE
    @Path("{movieId}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response deleteReview(@HeaderParam("authorization") String token,
                                 @PathParam("movieId") String movieId,
                                 JsonObject jo) {
        try{
            Long userId = Tokens.fromToken(token);
            String jpql = "SELECT r FROM Review r WHERE r.userId=:userId AND r.movieId=:movieId";
            Review r = em.createQuery(jpql, Review.class)
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .getSingleResult();
            if(r != null){
                mz.getReviews().delete(r.getId());
                return Response.noContent().build();
            }
            else
                return Response.status(400).build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }
}
