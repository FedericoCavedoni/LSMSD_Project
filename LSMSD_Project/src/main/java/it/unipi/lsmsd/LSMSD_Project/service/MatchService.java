package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.BoardGameRepository;
import it.unipi.lsmsd.LSMSD_Project.dao.MatchRepository;
import it.unipi.lsmsd.LSMSD_Project.dao.ReviewRepository;
import it.unipi.lsmsd.LSMSD_Project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public int getAveragePlayingTimeByGameId(Long gameId) {
        List<Match> matches = matchRepository.findByGameId(gameId);
        return (int) matches.stream().mapToDouble(Match::getDuration).average().orElse(0.0);
    }


    public void updateAllAveragePlayingTime(String date) {
        List<Match> recentMatches;
        if (date != null) {
            recentMatches = matchRepository.findMatchesAfterDate(date);
        } else {
            recentMatches = matchRepository.findAll();
        }

        Set<Long> gameIds = recentMatches.stream().map(Match::getGameId).collect(Collectors.toSet());
        List<BoardGame> boardGames = boardGameRepository.findByGameIdIn(gameIds);

        for (BoardGame game : boardGames) {
            int averagePlayingTime = getAveragePlayingTimeByGameId(game.getGameId());

            game.setAveragePlayingTime(averagePlayingTime);
            boardGameRepository.save(game);
        }
    }



    public TopPlayerStatistic getTopPlayerByGameId(long gameId, int minMatches) {
        return matchRepository.findTopPlayerByGameId(gameId, minMatches);
    }


    public List<TopGameStatistic> getMostPlayedGameByMatches(int limit) {
        return matchRepository.findMostPlayedGameByMatches(limit);  // Ritorna una lista di TopGameStatistic
    }

    public List<TopGameStatistic> getMostPlayedGameByTime(int limit) {
        return matchRepository.findMostPlayedGameByTime(limit);
    }

    public List<Match> getMatchesByGameId(long gameId, int limit) {

        Pageable pageable = PageRequest.of(0, limit);
        return matchRepository.findByGameIdWithLimit(gameId, pageable);
    }

    public List<Match> getMatchesByUserAndGame(String username, long gameId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return matchRepository.findByUserAndGameIdWithLimit(username, gameId, pageable);
    }


    public UserGameStatistic getStatisticsByUser(String username) {
        return matchRepository.findStatisticsByUser(username);
    }



}
