package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.GameStatistic;
import it.unipi.lsmsd.LSMSD_Project.model.Match;
import it.unipi.lsmsd.LSMSD_Project.model.TopPlayerStatistic;
import it.unipi.lsmsd.LSMSD_Project.model.UserGameStatistic;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MatchRepository extends MongoRepository<Match, String> {
    List<Match> findByUser(String user);


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
}
