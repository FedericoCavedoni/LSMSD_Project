package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MatchRepository extends MongoRepository<Match, String> {
    List<Match> findByUser(String user);
    List<Match> findByUserAndGameId(String user, long gameId);


    @Aggregation(pipeline = {
            "{ $group: { _id: '$game', totalMatches: { $sum: 1 }, totalWins: { $sum: { $cond: ['$result', 1, 0] } }, avgDuration: { $avg: '$duration' } } }",
            "{ $addFields: { winRate: { $multiply: [ { $divide: ['$totalWins', '$totalMatches'] }, 100 ] } } }",
            "{ $match: { $expr: { $gte: [ '$totalMatches', ?0 ] } } }",
            "{ $sort: { winRate: ?2 } }",
            "{ $limit: ?1 }",
            "{ $project: { _id: 0, game: '$_id', totalMatches: 1, totalWins: 1, avgDuration: 1, winRate: 1 } }"
    })
    List<GameStatistic> getGameStatistics(Integer minMatches, Integer limit, Integer sortOrder);


    @Aggregation(pipeline = {
            "{ $group: { _id: '$user', totalMatches: { $sum: 1 }, totalWins: { $sum: { $cond: ['$result', 1, 0] } }, mostPlayedGame: { $first: '$game' }, leastPlayedGame: { $last: '$game' } } }",
            "{ $addFields: { winRate: { $multiply: [ { $divide: ['$totalWins', '$totalMatches'] }, 100 ] } } }",
            "{ $sort: { totalMatches: ?1 } }",
            "{ $limit: ?0 }",
            "{ $project: { _id: 0, user: '$_id', totalMatches: 1, winRate: 1, mostPlayedGame: 1, leastPlayedGame: 1 } }"
    })
    List<UserGameStatistic> getUserGameStatistics(Integer limit, Integer sortOrder);


    @Aggregation(pipeline = {
            "{ $group: { _id: { game: '$game', user: '$user' }, totalMatches: { $sum: 1 }, totalWins: { $sum: { $cond: ['$result', 1, 0] } } } }",
            "{ $addFields: { winRate: { $multiply: [ { $divide: ['$totalWins', '$totalMatches'] }, 100 ] } } }",
            "{ $match: { $expr: { $gte: [ '$totalMatches', ?1 ] } } }",
            "{ $group: { _id: '$_id.game', topPlayer: { $first: { user: '$_id.user', totalMatches: '$totalMatches', winRate: '$winRate' } } } }",
            "{ $sort: { '_id': 1 } }",
            "{ $project: { _id: 0, game: '$_id', user: '$topPlayer.user', totalMatches: '$topPlayer.totalMatches', winRate: '$topPlayer.winRate' } }",
            "{ $limit: ?0 }"
    })
    List<TopPlayerStatistic> getTopPlayersForEachGame(Integer limit, Integer minMatches);


    @Aggregation(pipeline = {
            "{ $group: { _id: '$user', totalMatches: { $sum: 1 }, totalWins: { $sum: { $cond: ['$result', 1, 0] } }, avgNumGiocatori: { $avg: '$numgiocatori' } } }",
            "{ $addFields: { winRate: { $multiply: [ { $divide: ['$totalWins', '$totalMatches'] }, 100 ] }, weightedWinRate: { $multiply: [{ $divide: ['$totalWins', '$totalMatches'] }, '$avgNumGiocatori'] } } }",
            "{ $match: { $expr: { $gte: [ '$totalMatches', ?1 ] } } }",
            "{ $sort: { avgNumGiocatori: -1 } }",
            "{ $project: { _id: 0, user: '$_id', avgNumGiocatori: 1, winRate: 1, weightedWinRate: 1, totalMatches: 1 } }",
            "{ $limit: ?0 }"
    })
    List<TopAvgPlayersStatistic> findUsersWithHighestAvgNumGiocatori(Integer limit, Integer minMatches);

    @Aggregation(pipeline = {
            "{ $match: { 'gameId': ?0 } }",
            "{ $group: { _id: '$user', totalMatches: { $sum: 1 }, totalWins: { $sum: { $cond: ['$result', 1, 0] } }, game: { $first: '$game' } } }",
            "{ $addFields: { winRate: { $multiply: [ { $divide: ['$totalWins', '$totalMatches'] }, 100 ] } } }",
            "{ $sort: { winRate: -1 } }",
            "{ $limit: 1 }",
            "{ $project: { _id: 0, user: '$_id', game: 1, winRate: 1, totalMatches: 1 } }"
    })
    TopPlayerStatistic findTopPlayerByGameId(long gameId);

    @Aggregation(pipeline = {
            "{ $group: { _id: '$game', totalMatches: { $sum: 1 } } }",
            "{ $sort: { totalMatches: -1 } }",
            "{ $limit: 1 }",
            "{ $project: { _id: 0, game: '$_id', totalMatches: 1 } }"
    })
    TopGameStatistic findMostPlayedGameByMatches();


    @Aggregation(pipeline = {
            "{ $group: { _id: '$game', totalTimePlayed: { $sum: '$duration' } } }",
            "{ $sort: { totalTimePlayed: -1 } }",
            "{ $limit: 1 }",
            "{ $project: { _id: 0, game: '$_id', totalTimePlayed: 1 } }"
    })
    TopGameStatistic findMostPlayedGameByTime();


    List<Match> findByGameId(Long gameId);

    @Query("{ 'date': { $gte: ?0 } }")
    List<Match> findMatchesAfterDate(String date);

    @Query(value = "{ 'gameId': ?0 }")
    List<Match> findByGameIdWithLimit(long gameId, Pageable pageable);




}
