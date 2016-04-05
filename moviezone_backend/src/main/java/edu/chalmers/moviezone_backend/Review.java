/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Space
 */
@Entity
@Table(name = "REVIEW", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"MOVIEID","USERID"})
})
public class Review extends AbstractEntity implements Serializable{
    
    @Getter
    @Setter
    private String movieId;
    @Getter
    @Setter
    private Long userId;
    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private String reviewText;
    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewDate;
    
    protected Review(){};
    
    public Review(String movie, Long userId, String user, String text){
        movieId = movie;
        this.userId = userId;
        userName = user;
        reviewText = text;
        reviewDate = new Date();
    }
    
    public Review(Long id, String movie, Long userId, String user, String text){
        super(id);
        movieId = movie;
        this.userId = userId;
        userName = user;
        reviewText = text;
        reviewDate = new Date(System.currentTimeMillis());
    }
 
    @Override
    public String toString() {
        return "Review{" + "movieId=" + getMovieId() + ", userId=" + getUserId() + ", user=" + getUserName() +  ", reviewText=" + getReviewText() + ", reviewDate=" + getReviewDate() + '}';
    }
    
}
