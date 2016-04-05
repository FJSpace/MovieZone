/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend;

import edu.chalmers.moviezone_backend.persistence.IDAO;
import java.util.List;

/**
 *
 * @author Space
 */
public interface IEnjoyers extends IDAO<Enjoyer,Long>{
    public List<Enjoyer> getByName(String fname);
    
    public List<Enjoyer> getByEmail(String email);
    
    public String getUserById(Long id);
}
