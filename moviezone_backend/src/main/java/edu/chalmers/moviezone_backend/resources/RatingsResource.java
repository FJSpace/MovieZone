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

     @PersistenceContext(unitName = "moviezone_pu",type=PersistenceContextType.TRANSACTION)
    private EntityManager em;
   
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
            String jpql = "SELECT r FROM Rate r WHERE r.userId=:userId AND r.movieId=:movieId";
            Rate r = em.createQuery(jpql, Rate.class).setParameter("userId", userId).setParameter("movieId", movieId).getSingleResult();
            if(r != null)
                return Response.ok(r).build();
            else
                return Response.status(400).build();
        }
        catch(JoseException je) { return Response.status(401).build(); }
        catch(NoResultException nre){ return Response.status(417).build(); }
    }
     
    @GET
    @Path("{movieId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAverageRating(@PathParam("movieId") String movieId){ 
        try{
            String jpql = "SELECT AVG(r.rating) FROM Rate r WHERE r.movieId=:movieId GROUP BY r.movieId";
            double avg = em.createQuery(jpql, double.class)
                    .setParameter("movieId", movieId).getSingleResult();
            return Response.ok(avg).build();
        } catch(NoResultException nre){ return Response.status(417).build(); }
    }
    
    @GET
    @Path("top")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getTopRatedMovies(@QueryParam("nbrofmovies") int nrOfMovies){ 
        String jpql = "SELECT DISTINCT r.movieId FROM Rate r";
        List<String> movies = em.createQuery(jpql, String.class).getResultList();
        if(movies.isEmpty())
            return Response.status(417).build();
        else {
            MovieAvg ma = new MovieAvg();
            List<MovieAvg> mas = new ArrayList<>();
            double avg;
            for(String m : movies){
                avg = em.createQuery("SELECT AVG(r.rating) FROM Rate r WHERE r.movieId=:movieId GROUP BY r.movieId", double.class)
                    .setParameter("movieId", m).getSingleResult();
                ma = new MovieAvg(m,avg);
                mas.add(ma);
            }
            mas.sort(ma);
            mas.subList(0, nrOfMovies);
            Collection<MovieAvgWrapper> movWrap = new HashSet<>();
            mas.stream().forEachOrdered((movAvg) -> {
                movWrap.add(new MovieAvgWrapper(movAvg));
            });
            GenericEntity<Collection<MovieAvgWrapper>> ge = new GenericEntity<Collection<MovieAvgWrapper>>(movWrap) {};
            Gson gs = new Gson();
            return Response.ok(gs.toJson(mas)).build();
        }
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
            String jpql = "SELECT r FROM Rate r WHERE r.userId=:id AND r.movieId=:movieId";
            Rate r = em.createQuery(jpql, Rate.class)
                    .setParameter("movieId", jo.getString("movieId"))
                    .setParameter("userId",userId)
                    .getSingleResult();
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
            String jpql = "SELECT r FROM Rate r WHERE r.userId=:id AND r.movieId=:movieId";
            Rate r = em.createQuery(jpql, Rate.class)
                    .setParameter("userId", userId)
                    .setParameter("movieId", jo.getString("movieId"))
                    .getSingleResult();
            if(r != null){
                mz.getRatings().delete(r.getId());
                return Response.ok().build();
            }
            else
                return Response.status(400).build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }
    
      /**
     * Classes for the method getTopRatedMovies
     */
    public class MovieAvg implements Comparator<MovieAvg>{
        
        String movie;
        double avg;
        
        public MovieAvg(){}
        
        public MovieAvg(String movie, double avg){
            this.avg = avg;
            this.movie = movie;
        }

        @Override
        public int compare(MovieAvg d, MovieAvg d1) {
            return Double.compare(d.avg, d1.avg);
        }
    }
    
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.PROPERTY)
    @XmlType(name = "MovieAvg", propOrder = {
        "movie",
        "avg"
    })
    public class MovieAvgWrapper {
        private MovieAvg ma;
    
        protected MovieAvgWrapper(){}
    
        public MovieAvgWrapper(MovieAvg ma){
            this.ma = ma;
        }
    
        @XmlElement
        public String getMovie(){
            return ma.movie;
        }
    
        @XmlElement
        public double getMovieId(){
            return ma.avg;
        }
    }
}
