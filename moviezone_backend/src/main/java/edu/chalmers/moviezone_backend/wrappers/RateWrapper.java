/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend.wrappers;

import edu.chalmers.moviezone_backend.Rate;
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
    "rate"
})
public class RateWrapper {
    private Rate rate;
    
    protected RateWrapper(){}
    
    public RateWrapper(Rate r){
        this.rate = r;
    }
    
    @XmlElement
    public Long getId(){
        return rate.getId();
    }
    
    @XmlElement
    public String getMovieId(){
        return rate.getMovieId();
    }
    
    @XmlElement
    public Long getUserId(){
        return rate.getUserId();
    }
    
    @XmlElement
    public int getRating(){
        return rate.getRating();
    }
}
