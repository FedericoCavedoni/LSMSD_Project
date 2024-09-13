package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.Review;
import it.unipi.lsmsd.LSMSD_Project.projections.ReviewProjection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import org.springframework.data.mongodb.repository.Query;


public interface ReviewRepository extends MongoRepository<Review, Long> {
    List<Review> findByUsername(String username);
    List<Review> findByUsernameAndGameId(String username, Long gameId);
    List<Review> findByGameId(Long gameId);

    @Query("{ 'date': { $gte: ?0 } }")
    List<Review> findReviewsAfterDate(String date);

    @Query("{ 'gameId': ?0 }")
    List<ReviewProjection> findReviewsByGameId(long gameId);


}
