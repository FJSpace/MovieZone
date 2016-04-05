/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend;

import edu.chalmers.moviezone_backend.crypto.Password;
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
@Table(name = "Enjoyer", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"USERNAME"})
})
public class Enjoyer extends AbstractEntity {
    
    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private String fname = "";
    @Getter
    @Setter
    private String lname = "";
    @Getter
    @Setter
    private String email = "";
    @Getter
    private String password;

    public Enjoyer() {}

    public Enjoyer(String userName, String password) {
        this.userName = userName;
        this.password = Password.hashPassword(password);
    }

    public Enjoyer(Long id, String userName, String password) {
        super(id);
        this.userName = userName;
        this.password = password;
    }
  
    public void setPassword(String s){
        password = Password.hashPassword(s);
    }

    @Override
    public String toString() {
        return "Enjoyer{" + ", user=" + getUserName() + ", fname=" + getFname() + ", lname=" + getLname() + ", email=" + getEmail() + '}';
    }
    
}
