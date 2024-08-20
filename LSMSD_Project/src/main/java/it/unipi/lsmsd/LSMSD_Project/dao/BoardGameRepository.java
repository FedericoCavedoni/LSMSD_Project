package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import it.unipi.lsmsd.LSMSD_Project.projections.BoardGameLimitedProjection;
import it.unipi.lsmsd.LSMSD_Project.projections.BoardGameNameProjection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Map;

public interface BoardGameRepository extends MongoRepository<BoardGame, String> {
    BoardGame findByName(String name);

    @Query(value = "{}", fields = "{ 'name' : 1, 'category' : 1, 'minPlayers' : 1, 'maxPlayers' : 1, 'rating' : 1, 'playingTime' : 1, 'reviews.text' : 1 }")
    List<BoardGameLimitedProjection> findLimitedBoardGames();

    @Query(value = "{ 'rating' : { $gte: ?0 } }", fields = "{ 'name' : 1, 'category' : 1, 'minPlayers' : 1, 'maxPlayers' : 1, 'rating' : 1, 'playingTime' : 1, 'reviews.text' : 1 }")
    List<BoardGameLimitedProjection> findBoardGamesWithRatingGreaterThanEqual(float rating);

    @Query(value = "{ 'boardgamecategory' : { $in: ?0 } }", fields = "{ 'name' : 1, 'category' : 1, 'minPlayers' : 1, 'maxPlayers' : 1, 'rating' : 1, 'playingTime' : 1, 'reviews.text' : 1 }")
    List<BoardGameLimitedProjection> findBoardGamesByCategories(List<String> categories);

    @Query(value = "{ 'boardgamemechanic' : { $in: ?0 } }", fields = "{ 'name' : 1, 'boardgamemechanic' : 1, 'category' :  1, 'minPlayers' : 1, 'maxPlayers' : 1, 'rating' : 1, 'playingTime' : 1, 'reviews.text' : 1 }")
    List<BoardGameLimitedProjection> findBoardGamesByMechanics(List<String> mechanics);

    @Query(value = "{ 'name' : ?0 }", fields = "{ 'name' : 1 }")
    BoardGameNameProjection findBoardGameNameByName(String name);


}


