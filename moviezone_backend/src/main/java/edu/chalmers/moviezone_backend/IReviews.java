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
public interface IReviews extends IDAO<Review,Long>{
    public List<Review> getByText(String search);
    public List<Review> getByMovie(String movieId);
    public List<Review> getByEnjoyer(Long userid);
    public List<Review> getLatestByEnjoyer(Long userId, int end);
    public List<Review> getLatestReviews(int end);
    public Review getMyReview(Long userId,String movieId);
}
