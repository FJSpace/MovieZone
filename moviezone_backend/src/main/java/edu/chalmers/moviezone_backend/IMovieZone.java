/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.chalmers.moviezone_backend;

/**
 *
 * @author Space
 */
public interface IMovieZone {
    
    public IEnjoyers getEnjoyers();

    public IRatings getRatings();

    public IReviews getReviews();
}
