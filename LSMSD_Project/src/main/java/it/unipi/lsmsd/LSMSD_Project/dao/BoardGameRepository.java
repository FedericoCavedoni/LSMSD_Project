package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import it.unipi.lsmsd.LSMSD_Project.projections.BoardGameLimitedProjection;
import it.unipi.lsmsd.LSMSD_Project.projections.BoardGameNameProjection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BoardGameRepository extends MongoRepository<BoardGame, String> {
    BoardGame findByName(String name);
    BoardGame findByGameId(long gameId);

    @Query(value = "{}", fields = "{ 'name' : 1, 'category' : 1, 'minPlayers' : 1, 'maxPlayers' : 1, 'rating' : 1, 'playingTime' : 1}")
    List<BoardGameLimitedProjection> findLimitedBoardGames();

    @Query(value = "{ 'rating' : { $gte: ?0 } }", fields = "{ 'name' : 1, 'category' : 1, 'minPlayers' : 1, 'maxPlayers' : 1, 'rating' : 1, 'playingTime' : 1}")
    List<BoardGameLimitedProjection> findBoardGamesWithRatingGreaterThanEqual(float rating);

    @Query(value = "{ 'boardgamecategory' : { $in: ?0 } }", fields = "{ 'name' : 1, 'category' : 1, 'minPlayers' : 1, 'maxPlayers' : 1, 'rating' : 1, 'playingTime' : 1}")
    List<BoardGameLimitedProjection> findBoardGamesByCategories(List<String> categories);

    @Query(value = "{ 'boardgamemechanic' : { $in: ?0 } }", fields = "{ 'name' : 1, 'boardgamemechanic' : 1, 'category' :  1, 'minPlayers' : 1, 'maxPlayers' : 1, 'rating' : 1, 'playingTime' : 1}")
    List<BoardGameLimitedProjection> findBoardGamesByMechanics(List<String> mechanics);

    @Query(value = "{ 'name' : ?0 }", fields = "{ 'name' : 1 }")
    BoardGameNameProjection findBoardGameNameByName(String name);

    @Query(value = "{ 'name': { $regex: ?0, $options: 'i' } }")
    List<BoardGame> findByNameContainingIgnoreCase(String partialName);


    @Query("{ 'gameId' : { $in: ?0 } }")
    List<BoardGame> findByGameIdIn(Set<Long> gameIds);




}


