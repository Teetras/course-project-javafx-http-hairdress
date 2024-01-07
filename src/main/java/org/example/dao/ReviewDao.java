package org.example.dao;

import org.example.model.Review;

import java.util.List;

public interface ReviewDao {
    boolean addReview(Review review);
    boolean updateReview(Review review);
    boolean deleteReview(int id);
    List<Review> getAllReviews();
    Review findReviewById(int id);
}