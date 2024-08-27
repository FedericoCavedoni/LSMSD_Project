package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.BoardGameRepository;
import it.unipi.lsmsd.LSMSD_Project.dao.MatchRepository;
import it.unipi.lsmsd.LSMSD_Project.dao.ReviewRepository;
import it.unipi.lsmsd.LSMSD_Project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private BoardGameRepository boardGameRepository;

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

    public List<TopAvgPlayersStatistic> getUsersWithHighestAvgNumGiocatori(Integer limit, Integer minMatches) {
        int maxLimit = (limit != null) ? limit : Integer.MAX_VALUE;
        int matchLimit = (minMatches != null) ?  minMatches: 0;

        return matchRepository.findUsersWithHighestAvgNumGiocatori(maxLimit, matchLimit);
    }

    public float getAveragePlayingTimeByGameId(String gameId) {
        List<Match> matches = matchRepository.findByGameId(gameId);

        return (float) matches.stream().mapToDouble(Match::getDuration).average().orElse(0.0);
    }


    public void updateAllAveragePlayingTime() {
        List<BoardGame> boardGames = boardGameRepository.findAll();

        for (BoardGame game : boardGames) {
            float averagePlayingTime = getAveragePlayingTimeByGameId(String.valueOf(game.getGameId()));
            game.setAveragePlayingTime(averagePlayingTime);
            boardGameRepository.save(game);
        }
    }

}
