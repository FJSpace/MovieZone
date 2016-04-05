/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend.resources;

import com.google.gson.Gson;
import edu.chalmers.moviezone_backend.MovieZone;
import edu.chalmers.moviezone_backend.Rate;
import edu.chalmers.moviezone_backend.crypto.Tokens;
import edu.chalmers.moviezone_backend.wrappers.RateWrapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jose4j.lang.JoseException;

/**
 *
 * @author Rasti
 */
@Stateless
@LocalBean
@Path("ratings")
public class RatingsResource {

    //@PersistenceContext(unitName = "moviezone_pu",type=PersistenceContextType.TRANSACTION)
    //private EntityManager em;
   
    @Context
    private UriInfo uriInfo;
    
    @Inject
    private MovieZone mz;
    
    @GET
    @Path("user/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRatingsByUser(@PathParam("id") Long id){ 
        Gson gs = new Gson();
        List<Rate> rates = mz.getRatings().getByEnjoyer(id);
        return Response.ok(gs.toJson(rates)).build();
    }
     
    @GET
    @Path("movie/{movieId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMyRating(@HeaderParam("authorization") String token,
                                @PathParam("movieId") String movieId ) {
        try{
            Long userId = Tokens.fromToken(token);
            Rate r = mz.getRatings().getMyRating(userId, movieId);
            if(r != null)
                return Response.ok(r).build();
            else
                return Response.status(400).build();
        }
        catch(JoseException je) { return Response.status(401).build(); }
    }
     
    @GET
    @Path("{movieId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAverageRating(@PathParam("movieId") String movieId){ 
        double avg = mz.getRatings().getAverageRating(movieId);
        return Response.ok(avg).build();
    }
    
    @GET
    @Path("top")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getTopRatedMovies(@QueryParam("nbrofmovies") int nrOfMovies){ 
        Gson gs = new Gson();
        List<Rate> rs = mz.getRatings().getTopRatedMovies();
        while(rs.size() > nrOfMovies){
            Rate r = rs.get(rs.size()-1);
            rs.remove(rs.size()-1);
            mz.getRatings().delete(r.getId());
        }
        String json = gs.toJson(rs);
        while(!rs.isEmpty()){
            Rate r = rs.get(rs.size()-1);
            rs.remove(rs.size()-1);
            mz.getRatings().delete(r.getId());
        }
        return Response.ok(json).build();
    }
    
    @POST
    @Path("movie/{movieId}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response PostRating(@HeaderParam("authorization") String token,
                               @PathParam("movieId") String movieId,
                               JsonObject jo) {
        try{
            Long userId = Tokens.fromToken(token);
            Rate r = new Rate(movieId,userId,jo.getInt("rating"));
            mz.getRatings().create(r);
            URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(r.getId())).build(r);
            return Response.created(uri).build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }
    
    @PUT
    @Path("{id}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response editRating(@HeaderParam("authorization") String token,
                               JsonObject jo) {
        try{
            Long userId = Tokens.fromToken(token);
            String movieId = jo.getString("movieId");
            Rate r = mz.getRatings().getMyRating(userId, movieId);
            if(r != null){
                r.setRating(jo.getInt("rating"));
                mz.getRatings().update(r);
                return Response.noContent().build();
            }
            else
                return Response.status(400).build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }
     
    @DELETE
    @Path("movie/{movieId}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response deleteRate(@HeaderParam("authorization") String token,
                                 JsonObject jo) {
        try{
            Long userId = Tokens.fromToken(token);
            String movieId = jo.getString("movieId");
            Rate r = mz.getRatings().getMyRating(userId, movieId);
            if(r != null){
                mz.getRatings().delete(r.getId());
                return Response.ok().build();
            }
            else
                return Response.status(400).build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }
}
