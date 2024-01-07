package org.example.service.serviceImpl;

import org.example.dao.ReviewDao;
import org.example.dao.daoImpl.ReviewDaoImpl;
import org.example.model.Review;
import org.example.service.ReviewService;
import org.hibernate.HibernateError;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    private ReviewDao reviewDao = new ReviewDaoImpl();

    @Override
    public boolean addReview(Review review) {
        boolean isAdded = false;
        try {
            if (reviewDao.addReview(review))
                isAdded = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isAdded;
    }

    @Override
    public boolean updateReview(Review review) {
        boolean isUpdated = false;
        try {
            if (reviewDao.updateReview(review))
                isUpdated = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteReview(int id) {
        boolean isDeleted = false;
        try {
            if (reviewDao.deleteReview(id))
                isDeleted = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isDeleted;
    }

    @Override
    public Review findReviewById(int id) {
        Review review = null;
        try {
            review = reviewDao.findReviewById(id);
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return review;
    }

    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = null;
        try {
            reviews = reviewDao.getAllReviews();
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return reviews;
    }
}