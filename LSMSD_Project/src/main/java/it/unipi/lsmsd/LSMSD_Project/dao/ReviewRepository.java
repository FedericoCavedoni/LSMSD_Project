package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByGame(Long gameId);
    List<Review> findByUsername(String username);
    List<Review> findByUsernameAndGame(String username, Long gameId);
}
