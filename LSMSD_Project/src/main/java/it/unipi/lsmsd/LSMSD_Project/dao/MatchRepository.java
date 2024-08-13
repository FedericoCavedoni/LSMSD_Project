package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.GameStatistic;
import it.unipi.lsmsd.LSMSD_Project.model.Match;
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
}
