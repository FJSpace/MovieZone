/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend;

import edu.chalmers.moviezone_backend.resources.RatingsResource;
import java.io.Serializable;
import java.util.Comparator;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Space
 */
@Entity
@Table(name = "RATE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"MOVIEID","USERID"})
})
public class Rate extends AbstractEntity implements Serializable, Comparator<Rate> {
    
    @Getter
    @Setter
    private String movieId;
    @Getter
    @Setter
    private Long userId;
    @Getter
    @Setter
    private double rating;
    
    protected Rate(){};
    
    public Rate(String movie, Long user, double rate){
        movieId = movie;
        userId = user;
        rating = rate;
    }
    
    public Rate(Long id, String movie, Long user, double rate){
        super(id);
        movieId = movie;
        userId = user;
        rating = rate;
    }
   
    @Override
    public int compare(Rate d, Rate d1) {
        return Double.compare(d.rating, d1.rating);
    }
    
    @Override
    public String toString(){
        return "Rate{" + "movieId=" + getMovieId() + ", userId=" + getUserId() + ", rating=" + getRating() + '}';
    }
}
