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
public class Ratings extends DAO<Rate,Long> implements IRatings{

    @PersistenceContext
    private EntityManager em;

    public Ratings() {
        super(Rate.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    //Find all ratings with some specific rate (1-5)
    @Override
    public List<Rate> getByRate(int rate) {
        String jpql = "SELECT r FROM Rate r WHERE r.rating=:rate";
        return em.createQuery(jpql, Rate.class).
                setParameter("rate", rate).getResultList();
    }

    //Find all ratings to one movie
    @Override
    public List<Rate> getByMovie(String movieId) {
        String jpql = "SELECT r FROM Rate r WHERE r.movieId=:movieId";
        return em.createQuery(jpql, Rate.class).
                setParameter("movieId", movieId).getResultList();
    }

    //Find all ratings one user have made
    @Override
    public List<Rate> getByEnjoyer(Long userId) {
        String jpql = "SELECT r FROM Rate r WHERE r.userId=:userId";
        return em.createQuery(jpql, Rate.class).
                setParameter("userId", userId).getResultList();
    }
    
}
