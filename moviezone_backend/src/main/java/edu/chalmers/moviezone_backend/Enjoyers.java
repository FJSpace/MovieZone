/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend;

import edu.chalmers.moviezone_backend.persistence.DAO;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Space
 */
@Stateless
public class Enjoyers extends DAO<Enjoyer,Long> implements IEnjoyers{
    
    @PersistenceContext
    private EntityManager em;

    public Enjoyers() {
        super(Enjoyer.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    //To find enjoyers by searching on ther "real" name
    @Override
    public List<Enjoyer> getByName(String fname){
        String jpql = "SELECT e FROM Enjoyer e WHERE e.Fname=:Fname";
        return em.createQuery(jpql, Enjoyer.class).
                setParameter("Fname", fname).getResultList();
    }
    
    //To find enjoyers by searching on ther email. Can be used to find duplicates
    @Override
    public List<Enjoyer> getByEmail(String email){
        String jpql = "SELECT e FROM Enjoyer e WHERE e.email=:email";
        return em.createQuery(jpql, Enjoyer.class).
                setParameter("email", email).getResultList();
    }
    
    @Override
    public String getUserById(Long id){
        String jpql = "SELECT e FROM Enjoyer e WHERE e.id=:id";
        Enjoyer e = em.createQuery(jpql, Enjoyer.class).
                setParameter("id", id).getSingleResult();
        return e.getUserName();
    }
}
