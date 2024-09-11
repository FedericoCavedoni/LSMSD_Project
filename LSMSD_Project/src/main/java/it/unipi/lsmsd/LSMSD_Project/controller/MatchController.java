package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.model.*;
import it.unipi.lsmsd.LSMSD_Project.service.MatchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
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

        if (currentUser == null) {
            return new ResponseEntity<>("Operazione non autorizzata: Utente non autenticato.", HttpStatus.UNAUTHORIZED);
        }
        if (currentUser.getUsername() != match.getUser()) {
            return new ResponseEntity<>("Operazione non autorizzata: Non puoi aggiungere un match per un altro utente.", HttpStatus.UNAUTHORIZED);
        }
        match.setUser(currentUser.getUsername());



        Match newMatch = matchService.addMatch(match);

        return ResponseEntity.ok(newMatch);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllMatches(HttpSession session, @RequestParam(required = false) int limit) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
            List<Match> matches = matchService.getAllMatches(limit);
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
    public ResponseEntity<TopPlayerStatistic> getTopPlayerByGameId(
            @RequestParam long gameId,
            @RequestParam(required = false, defaultValue = "3") int minMatches) {

        TopPlayerStatistic topPlayer = matchService.getTopPlayerByGameId(gameId, minMatches);

        if (topPlayer != null) {
            return ResponseEntity.ok(topPlayer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/mostPlayedGameByMatches")
    public ResponseEntity<List<Map<String, Object>>> getMostPlayedGameByMatches(
            @RequestParam(defaultValue = "5") int limit) {
        List<TopGameStatistic> topGames = matchService.getMostPlayedGameByMatches(limit);

        if (!topGames.isEmpty()) {
            List<Map<String, Object>> responseList = new ArrayList<>();
            for (TopGameStatistic topGame : topGames) {
                Map<String, Object> response = new HashMap<>();
                response.put("game", topGame.getGame());
                response.put("totalMatches", topGame.getTotalMatches());
                responseList.add(response);
            }
            return ResponseEntity.ok(responseList);  // Restituisce la lista di giochi
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/mostPlayedGameByTime")
    public ResponseEntity<List<Map<String, Object>>> getMostPlayedGameByTime(
            @RequestParam(defaultValue = "5") int limit) {
        List<TopGameStatistic> topGames = matchService.getMostPlayedGameByTime(limit);

        if (!topGames.isEmpty()) {
            List<Map<String, Object>> responseList = new ArrayList<>();
            for (TopGameStatistic topGame : topGames) {
                Map<String, Object> response = new HashMap<>();
                response.put("game", topGame.getGame());
                response.put("totalTimePlayed", topGame.getTotalTimePlayed());
                responseList.add(response);
            }
            return ResponseEntity.ok(responseList);
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
            @RequestParam(required = false) String user,
            @RequestParam long gameId,
            @RequestParam(required = false, defaultValue = "10") int limit,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        

        if (currentUser == null) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (user != null && !currentUser.getUsername().equals(user) && !currentUser.isAdmin()) {

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (user == null) {
            user = currentUser.getUsername();
        }

        List<Match> matches = matchService.getMatchesByUserAndGame(user, gameId, limit);

        if (!matches.isEmpty()) {
            return ResponseEntity.ok(matches);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/getLoggedUserStatistics")
    public ResponseEntity<?> getLoggedUserStatistics(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            UserGameStatistic statistic = matchService.getStatisticsByUser(currentUser.getUsername());
            if (statistic != null) {
                return ResponseEntity.ok(statistic);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
    }


}
