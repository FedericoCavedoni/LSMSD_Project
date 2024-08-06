package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.ReviewRepository;
import it.unipi.lsmsd.LSMSD_Project.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review addReview(Review review) {
        String username = review.getUsername();
        String game = review.getGame();

        List<Review> reviews = reviewRepository.findByUsernameAndGame(username, game);

        if(!reviews.isEmpty()){
            return null;
        }

        return reviewRepository.save(review);
    }

    public boolean deleteReview(String username, String game) {
        List<Review> reviews = reviewRepository.findByUsernameAndGame(username, game);
        if (!reviews.isEmpty()) {
            reviewRepository.deleteAll(reviews);
            return true;
        }
        return false;
    }

    public List<Review> getReviewsByUsername(String username) {
        return reviewRepository.findByUsername(username);
    }

    public List<Review> getReviewsByGame(String game) {
        return reviewRepository.findByGame(game);
    }

    public double getAverageRatingByGame(String game) {
        List<Review> reviews = reviewRepository.findByGame(game);
        return reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }

    public List<Review> findReviewByUserAndGame(String username, String game) {
        return reviewRepository.findByUsernameAndGame(username, game);
    }
}
