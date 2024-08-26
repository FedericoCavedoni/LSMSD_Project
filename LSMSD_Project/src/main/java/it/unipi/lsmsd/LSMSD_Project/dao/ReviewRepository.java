package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByGameId(String id);

    List<Review> findByUsername(String username);
    List<Review> findByUsernameAndGame(String username, Long gameId);
}
