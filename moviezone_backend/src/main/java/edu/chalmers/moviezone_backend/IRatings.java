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
public interface IRatings extends IDAO<Rate,Long>{
    public List<Rate> getByRate(int rate);
    public List<Rate> getByMovie(String movieId);
    public List<Rate> getByEnjoyer(Long userid);
    public Rate getMyRating(Long userId, String movieId);
    public double getAverageRating(String movieId);
    public List<Rate> getTopRatedMovies();
}
