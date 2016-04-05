/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend.wrappers;

import edu.chalmers.moviezone_backend.Review;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Space
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "Review", propOrder = {
    "id",
    "movieid",
    "userid",
    "reviewtext",
    "reviewdate"
})
public class ReviewWrapper {
    
    private Review rev;
    
    protected ReviewWrapper(){}
    
    public ReviewWrapper(Review r){
        this.rev = r;
    }
    
    @XmlElement
    public Long getId(){
        return rev.getId();
    }
    
    @XmlElement
    public String getMovieId(){
        return rev.getMovieId();
    }
    
    @XmlElement
    public Long getUserId(){
        return rev.getUserId();
    }
    
    @XmlElement
    public String getReviewText(){
        return rev.getReviewText();
    }
    
    @XmlElement
    public Date getReviewDate(){
        return rev.getReviewDate();
    }
}
