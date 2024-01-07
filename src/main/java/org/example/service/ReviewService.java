package org.example.service;

import org.example.model.Review;

import java.util.List;

public interface ReviewService {
    boolean addReview(Review review);
    boolean updateReview(Review review);
    boolean deleteReview(int id);

    List<Review> getAllReviews();
    Review findReviewById(int id);
}
