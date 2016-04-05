/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend.test;

import edu.chalmers.moviezone_backend.Enjoyer;
import edu.chalmers.moviezone_backend.MovieZone;

/**
 *
 * @author Rasti
 */

public class InitTest {
    MovieZone mz = new MovieZone();
    
    public void createUsers(int numberOfUsers){
        for(int i = 1; i<=numberOfUsers; i++){
            
            mz.getEnjoyers().create(new Enjoyer("user" + i, "hello"));
        }
    }
}
