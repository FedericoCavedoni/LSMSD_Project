package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;

public interface ReviewRepository extends MongoRepository<Review, Long> {
    List<Review> findByUsername(String username);
    List<Review> findByUsernameAndGame(String username, Long gameId);
    List<Review> findByGameId(Long gameId);

    @Query("{ 'date' : { $gte: ?0 } }")
    List<Review> findReviewsAfterDate(LocalDate date);
}
