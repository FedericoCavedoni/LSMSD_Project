package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.ReviewRepository;
import it.unipi.lsmsd.LSMSD_Project.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review registerNewReview(Review review) {
        return reviewRepository.save(review);
    }
}
