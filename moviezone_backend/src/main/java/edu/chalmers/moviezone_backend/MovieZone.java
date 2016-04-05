/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author Space
 */
@Named
@ApplicationScoped
public class MovieZone implements IMovieZone{
    
    @EJB
    private IEnjoyers enjoyers;
    @EJB
    private IRatings ratings;
    @EJB
    private IReviews reviews;

    public MovieZone() {
        Logger.getAnonymousLogger().log(Level.INFO, "MovieZone is ALIVE :)");
    }

    @Override
    public IEnjoyers getEnjoyers() {
        return enjoyers;
    }

    @Override
    public IRatings getRatings() {
        return ratings;
    }

    @Override
    public IReviews getReviews() {
        return reviews;
    }
   
   
}
