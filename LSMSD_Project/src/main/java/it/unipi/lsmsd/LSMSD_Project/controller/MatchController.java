package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.model.*;
import it.unipi.lsmsd.LSMSD_Project.service.MatchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/add")
    public ResponseEntity<?> addMatch(@RequestBody Match match, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            Match newMatch = matchService.addMatch(match);
            return ResponseEntity.ok(newMatch);
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllMatches(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
            List<Match> matches = matchService.getAllMatches();
            if (!matches.isEmpty()) {
                return ResponseEntity.ok(matches);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getByUser")
    public ResponseEntity<?> getMatchesByUser(@RequestParam(required = false) String user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        if (user == null) {
            List<Match> matches = matchService.getMatchesByUser(currentUser.getUsername());
            if (!matches.isEmpty()) {
                return ResponseEntity.ok(matches);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            if (currentUser.isAdmin()) {
                List<Match> matches = matchService.getMatchesByUser(user);
                if (!matches.isEmpty()) {
                    return ResponseEntity.ok(matches);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @GetMapping("/gameStatistics")
    public ResponseEntity<?> getGameStatistics(
            @RequestParam(required = false) Integer minMatches,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Boolean ascending,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if(currentUser != null && currentUser.isAdmin()) {
            List<GameStatistic> statistics = matchService.getGameStatistics(minMatches, limit, ascending);
            if (!statistics.isEmpty()) {
                return ResponseEntity.ok(statistics);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/userStatistics")
    public ResponseEntity<?> getUserGameStatistics(
            @RequestParam(required = false) Integer n,
            @RequestParam(required = false) Boolean ascending,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
            List<UserGameStatistic> statistics = matchService.getUserGameStatistics(n, ascending);
            if (!statistics.isEmpty()) {
                return ResponseEntity.ok(statistics);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/topPlayers")
    public ResponseEntity<?> getTopPlayersForEachGame(
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer minMatches,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
            List<TopPlayerStatistic> statistics = matchService.getTopPlayersForEachGame(limit, minMatches);
            if (!statistics.isEmpty()) {
                return ResponseEntity.ok(statistics);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/topAvgPlayers")
    public ResponseEntity<?> findUsersWithHighestAvgNumGiocatori(
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer minMatches,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");

        if (currentUser != null && currentUser.isAdmin()) {
            List<TopAvgPlayersStatistic> result = matchService.getUsersWithHighestAvgNumGiocatori(limit, minMatches);
            if (!result.isEmpty()) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.notFound().build();
            }
        }else{
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/topPlayerByGame")
    public ResponseEntity<TopPlayerStatistic> getTopPlayerByGameId(@RequestParam long gameId) {
        TopPlayerStatistic topPlayer = matchService.getTopPlayerByGameId(gameId);
        if (topPlayer != null) {
            return ResponseEntity.ok(topPlayer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/mostPlayedGameByMatches")
    public ResponseEntity<Map<String, Object>> getMostPlayedGameByMatches() {
        TopGameStatistic topGame = matchService.getMostPlayedGameByMatches();
        if (topGame != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("game", topGame.getGame());
            response.put("totalMatches", topGame.getTotalMatches());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/mostPlayedGameByTime")
    public ResponseEntity<Map<String, Object>> getMostPlayedGameByTime() {
        TopGameStatistic topGame = matchService.getMostPlayedGameByTime();
        if (topGame != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("game", topGame.getGame());
            response.put("totalTimePlayed", topGame.getTotalTimePlayed());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getMatchesByGameId")
    public ResponseEntity<List<Match>> getMatchesByGameId(@RequestParam long gameId, @RequestParam(defaultValue = "10") int limit) {
        List<Match> matches = matchService.getMatchesByGameId(gameId, limit);
        if (!matches.isEmpty()) {
            return ResponseEntity.ok(matches);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/getByUserAndGame")
    public ResponseEntity<List<Match>> getMatchesByUserAndGame(
            @RequestParam String user,
            @RequestParam long gameId,
            @RequestParam(required = false, defaultValue = "10") int limit) {

        List<Match> matches = matchService.getMatchesByUserAndGame(user, gameId, limit);
        if (!matches.isEmpty()) {
            return ResponseEntity.ok(matches);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
