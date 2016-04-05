/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend;

import java.io.Serializable;
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
public class Rate extends AbstractEntity implements Serializable {
    
    @Getter
    @Setter
    private String movieId;
    @Getter
    @Setter
    private Long userId;
    @Getter
    @Setter
    private int rating;
    
    protected Rate(){};
    
    public Rate(String movie, Long user, int rate){
        movieId = movie;
        userId = user;
        rating = rate;
    }
    
    public Rate(Long id, String movie, Long user, int rate){
        super(id);
        movieId = movie;
        userId = user;
        rating = rate;
    }
   
    @Override
    public String toString(){
        return "Rate{" + "movieId=" + getMovieId() + ", userId=" + getUserId() + ", rating=" + getRating() + '}';
    }
}
