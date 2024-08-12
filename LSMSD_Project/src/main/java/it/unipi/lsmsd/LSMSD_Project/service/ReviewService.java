package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.ReviewRepository;
import it.unipi.lsmsd.LSMSD_Project.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

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

    public List<Review> getRecentReviews(String game, int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("game").is(game));
        query.with(Sort.by(Sort.Direction.DESC, "date"));
        query.limit(limit);

        return mongoTemplate.find(query, Review.class);
    }

    public List<Review> getFilteredReviews(String game, Integer minRating, Integer maxRating, Integer limit) {
        Query query = new Query();

        if (StringUtils.hasText(game)) {
            query.addCriteria(Criteria.where("game").is(game));
        }

        if (minRating != null && maxRating != null) {
            query.addCriteria(Criteria.where("rating").gte(minRating).lte(maxRating));
        } else if (minRating != null) {
            query.addCriteria(Criteria.where("rating").gte(minRating));
        } else if (maxRating != null) {
            query.addCriteria(Criteria.where("rating").lte(maxRating));
        }

        query.with(Sort.by(Sort.Direction.DESC, "date"));

        if (limit != null && limit > 0) {
            query.limit(limit);
        }

        return mongoTemplate.find(query, Review.class);
    }

    public List<Review> getTopNReviews(String game, int n) {
        Query query = new Query();

        query.addCriteria(Criteria.where("game").is(game));

        query.with(Sort.by(Sort.Direction.DESC, "rating"));

        query.limit(n);

        return mongoTemplate.find(query, Review.class);
    }

    public List<Review> getLowestNReviews(String game, int n) {
        Query query = new Query();

        query.addCriteria(Criteria.where("game").is(game));

        query.with(Sort.by(Sort.Direction.ASC, "rating"));

        query.limit(n);

        return mongoTemplate.find(query, Review.class);
    }
}
