/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend.wrappers;

import edu.chalmers.moviezone_backend.Enjoyer;
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
@XmlType(name = "user", propOrder = {
    "id",
    "username",
    "fname",
    "lname",
    "email",
    "password"
})
public class EnjoyerWrapper {
    
    private Enjoyer enjoyer;
    
    protected EnjoyerWrapper(){}
    
    public EnjoyerWrapper(Enjoyer enjoyer){
        this.enjoyer = enjoyer;
    }
    
    @XmlElement
    public String getUsername(){
        return enjoyer.getUserName();
    }
    
    @XmlElement
    public Long getId(){
        return enjoyer.getId();
    }
    
    @XmlElement
    public String getFname(){
        return enjoyer.getFname();
    }
    
    @XmlElement
    public String getLname(){
        return enjoyer.getLname();
    }
    
    @XmlElement
    public String getEmail(){
        return enjoyer.getEmail();
    }
    
    @XmlElement
    public String getPassword(){
        return enjoyer.getPassword();
    }
}
