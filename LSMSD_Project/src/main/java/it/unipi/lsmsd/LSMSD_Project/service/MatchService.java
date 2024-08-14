package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.MatchRepository;
import it.unipi.lsmsd.LSMSD_Project.model.GameStatistic;
import it.unipi.lsmsd.LSMSD_Project.model.Match;
import it.unipi.lsmsd.LSMSD_Project.model.TopPlayerStatistic;
import it.unipi.lsmsd.LSMSD_Project.model.UserGameStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public Match addMatch(Match match) {
        return matchRepository.save(match);
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public List<Match> getMatchesByUser(String user) {
        return matchRepository.findByUser(user);
    }

    public List<GameStatistic> getGameStatistics(Integer minMatches, Integer limit, Boolean ascending) {
        int numMatches = (minMatches == null) ? 0 : minMatches;
        int maxLimit = (limit != null) ? limit : Integer.MAX_VALUE;
        int sortOrder = (ascending == null || ascending) ? -1 : 1;
        return matchRepository.getGameStatistics(numMatches, maxLimit, sortOrder);
    }

    public List<UserGameStatistic> getUserGameStatistics(Integer limit, Boolean ascending) {
        int maxLimit = (limit != null) ? limit : Integer.MAX_VALUE;
        int sortOrder = (ascending != null && ascending) ? -1 : 1;
        return matchRepository.getUserGameStatistics(maxLimit, sortOrder);
    }

    public List<TopPlayerStatistic> getTopPlayersForEachGame(Integer limit, Integer minMatches) {
        int maxLimit = (limit != null) ? limit : Integer.MAX_VALUE;
        int matchLimit = (minMatches != null) ?  minMatches: 0;

        return matchRepository.getTopPlayersForEachGame(maxLimit, matchLimit);
    }
}
