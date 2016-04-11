/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend;

import edu.chalmers.moviezone_backend.persistence.DAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    
    @Override
    public Rate getMyRating(Long userId, String movieId){
        try{
            String jpql = "SELECT r FROM Rate r WHERE r.userId=:userId AND r.movieId=:movieId";
            Rate r = em.createQuery(jpql, Rate.class)
                       .setParameter("userId", userId)
                       .setParameter("movieId", movieId)
                       .getSingleResult();
            return r;
        } catch(NoResultException nre) {return null;}
    }
    
    @Override
    public double getAverageRating(String movieId){
        try{
            String jpql = "SELECT AVG(r.rating) FROM Rate r WHERE r.movieId=:movieId GROUP BY r.movieId";
            double avg = em.createQuery(jpql, double.class)
                       .setParameter("movieId", movieId)
                       .getSingleResult();
            return avg;
        } catch(NoResultException nre) {return 0;}
    }
    
    @Override
    public List<Rate> getTopRatedMovies(){
        String jpql = "SELECT DISTINCT r.movieId FROM Rate r";
        List<String> movies = em.createQuery(jpql, String.class).getResultList();
        List<Rate> rs = new ArrayList<>();
        if(movies.isEmpty())
            return rs;
        else {
            Rate r = new Rate();
            rs = new ArrayList<>();
            double avg;
            for(String m : movies){
                avg = em.createQuery("SELECT AVG(r.rating) FROM Rate r WHERE r.movieId=:movieId GROUP BY r.movieId", double.class)
                    .setParameter("movieId", m).getSingleResult();
                r = new Rate(m,null,avg);
                rs.add(r);
            }
            rs.sort(r);
            Collections.reverse(rs);
            return rs;
        }
    }
    
}
