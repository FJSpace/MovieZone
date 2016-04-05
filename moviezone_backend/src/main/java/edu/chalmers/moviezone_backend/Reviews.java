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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Space
 */
@Stateless
public class Reviews extends DAO<Review,Long> implements IReviews{

    @PersistenceContext
    private EntityManager em;

    public Reviews() {
        super(Review.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    //Find all reviews that have the string in search somewhere in the text.
    @Override
    public List<Review> getByText(String search) {
        String jpql = "SELECT r FROM Review r WHERE r.reviewText LIKE :search";
        return em.createQuery(jpql, Review.class).
                setParameter("search", "%"+search+"%").getResultList();
    }
    
    //Find all reviews to one movie
    @Override
    public List<Review> getByMovie(String movieId) {
        String jpql = "SELECT r FROM Review r WHERE r.movieId=:movieId";
        return em.createQuery(jpql, Review.class).
                setParameter("movieId", movieId).getResultList();
    }

    //Find all review one user have written
    @Override
    public List<Review> getByEnjoyer(Long userid) {
        String jpql = "SELECT r FROM Review r WHERE r.userId=:userid";
        return em.createQuery(jpql, Review.class).
                setParameter("userid", userid).getResultList();
    }
    
    @Override
    public List<Review> getLatestByEnjoyer(Long userId, int end){
        String jpql = "SELECT r FROM Review r WHERE r.userId=:userid ORDER BY r.reviewDate DESC";
        List<Review> rs = em.createQuery(jpql, Review.class).
                setParameter("userid", userId).getResultList();
        if(rs.size() <= end)
            return rs;
        else
            return rs.subList(0, end);
    }
    
    @Override
    public List<Review> getLatestReviews(int end){
        String jpql = "SELECT r FROM Review r ORDER BY r.reviewDate DESC";
        List<Review> rs = em.createQuery(jpql, Review.class).getResultList();
        if(rs.size() <= end)
            return rs;
        else
            return rs.subList(0, end);
    }
    
    @Override
    public Review getMyReview(Long userId, String movieId){
        try{
            String jpql = "SELECT r FROM Review r WHERE r.userId=:userId AND r.movieId=:movieId";
            Review r = em.createQuery(jpql, Review.class)
                         .setParameter("userId", userId)
                         .setParameter("movieId", movieId)
                         .getSingleResult();
            return r;
        } catch(NoResultException nre) {return null;}
    }
    
}
