package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import it.unipi.lsmsd.LSMSD_Project.projections.BoardGameLimitedProjection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Map;

public interface BoardGameRepository extends MongoRepository<BoardGame, String> {
    BoardGame findByName(String name);

    @Query(value = "{}", fields = "{ 'name' : 1, 'category' : 1, 'minPlayers' : 1, 'maxPlayers' : 1, 'rating' : 1, 'playingTime' : 1, 'reviews.text' : 1 }")
    List<BoardGameLimitedProjection> findLimitedBoardGames();
}


