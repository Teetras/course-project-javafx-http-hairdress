package org.example.dao.daoImpl;

import org.example.dao.ReviewDao;
import org.example.model.Review;
import org.example.sessionFactory.SessionFactoryImpl;
import org.example.utils.SessionUtils;

import java.util.List;

public class ReviewDaoImpl implements ReviewDao {

    @Override
    public boolean addReview(Review review) {
        return SessionUtils.saveEntity(review);
    }

    @Override
    public boolean updateReview(Review review) {
        return SessionUtils.updateEntity(review);
    }

    @Override
    public boolean deleteReview(int id) {
        return SessionUtils.deleteEntity(id, Review.class);
    }

    @Override
    public List<Review> getAllReviews() {

//        SessionFactory sessionFactory = Persistence.createEntityManagerFactory("my_persistence_unit");

        return (List<Review>) SessionFactoryImpl.getSessionFactory()
                .openSession()
                .createQuery("FROM Review")
                .list();
    }

    @Override
    public Review findReviewById(int id) {
        return SessionUtils.find(Review.class, id, "id");
    }
}