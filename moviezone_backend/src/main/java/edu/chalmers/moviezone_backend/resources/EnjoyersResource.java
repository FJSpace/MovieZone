/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend.resources;

import com.google.gson.Gson;
import edu.chalmers.moviezone_backend.MovieZone;
import edu.chalmers.moviezone_backend.Enjoyer;
import edu.chalmers.moviezone_backend.Review;
import edu.chalmers.moviezone_backend.Reviews;
import edu.chalmers.moviezone_backend.crypto.Password;
import edu.chalmers.moviezone_backend.crypto.Tokens;
import edu.chalmers.moviezone_backend.wrappers.EnjoyerWrapper;
import java.net.URI;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.jose4j.lang.JoseException;

/**
 *
 * @author Rasti
 */

@Stateless
@LocalBean
@Path("users")
public class EnjoyersResource {
  
    //@PersistenceContext(unitName = "moviezone_pu",type=PersistenceContextType.TRANSACTION)
    //private EntityManager em;
   
    @Context
    private UriInfo uriInfo;
    
    @Inject
    private MovieZone mz;
   
    //To find one specific user
    @GET
    @Path("{id: \\d+}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getProfile(@PathParam("id") Long id) {
        Enjoyer userToFind = mz.getEnjoyers().find(id);
        if (userToFind != null)
            return Response.ok(new EnjoyerWrapper(userToFind)).build(); //200 respons
        else
            return Response.noContent().build();  // 204 respons
    }
    
    //To find the user that is logged on
    @GET
    @Path("me")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMyProfile(@HeaderParam("authorization") String token){
        try{
            Gson gs = new Gson();
            Long id = Tokens.fromToken(token);
            Enjoyer me = mz.getEnjoyers().find(id);
            String userJson = gs.toJson(me);
            if(me != null){
                List<Review> rs = mz.getReviews().getByEnjoyer(me.getId());
                String revJson = gs.toJson(rs);
                String json = "{\"user\": " + userJson + ",\"reviews\":" + revJson + "}";
                return Response.ok(json).build();
            }else
                return Response.noContent().build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }
    
    //Register a new user
    @POST
    @Path("newuser")
    @Consumes(MediaType.APPLICATION_JSON)
     public Response register(JsonObject jo) throws JoseException{
        Enjoyer theUser = new Enjoyer(jo.getString("username"),jo.getString("password"));
        theUser.setEmail(jo.getString("email"));
        theUser.setFname(jo.getString("fname"));
        theUser.setLname(jo.getString("lname"));
        mz.getEnjoyers().create(theUser);
        String token = Tokens.toToken(theUser.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(token)).build(theUser);
        return Response.created(uri).entity(token).build();
    }
     
    //Logging in a user if userName and password is correct
    @POST
    @Path("login")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response login(JsonObject jo) {
        try{
            String username = jo.getString("username");
            Enjoyer user = mz.getEnjoyers().findByUsername(username);
            if(user != null){
                if(Password.checkPassword(jo.getString("password"), user.getPassword()))
                    return Response.ok(Tokens.toToken(user.getId())).build();
            }
            return Response.status(401).build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }
    
    //To edit a users profile
    @PUT
    @Path("me")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
     public Response editUser(@HeaderParam("authorization") String token,
                             JsonObject jo ) { 
        try{
            long id = Tokens.fromToken(token);
            Enjoyer theUser = new Enjoyer(id, jo.getString("username"),jo.getString("password"));
            theUser.setEmail(jo.getString("email"));
            theUser.setFname(jo.getString("fname"));
            theUser.setLname(jo.getString("lname"));
            mz.getEnjoyers().update(theUser);
            return Response.ok().build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }
    
    //Delete a user
    @DELETE
    @Path("me")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response deleteUser(@HeaderParam("authorization") String token) {
        try{    
            long id = Tokens.fromToken(token);
            mz.getEnjoyers().delete(id);
            return Response.ok().build();
        }catch(JoseException je) { return Response.status(401).build(); }
    }  
}
