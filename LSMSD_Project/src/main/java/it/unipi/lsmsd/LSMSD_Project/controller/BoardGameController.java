package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import it.unipi.lsmsd.LSMSD_Project.model.User;
import it.unipi.lsmsd.LSMSD_Project.projections.BoardGameLimitedProjection;
import it.unipi.lsmsd.LSMSD_Project.service.BoardGameService;
import it.unipi.lsmsd.LSMSD_Project.service.MatchService;
import it.unipi.lsmsd.LSMSD_Project.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/boardgames")
public class BoardGameController {

    @Autowired
    private BoardGameService boardGameService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MatchService matchService;


    @GetMapping("/getBoardGames")
    public ResponseEntity<List<BoardGame>> getBoardGames() {
        List<BoardGame> boardGames = boardGameService.getAllBoardGames();
        return ResponseEntity.ok(boardGames);
    }

    @GetMapping("/getBoardGameByName")
    public ResponseEntity<List<BoardGame>> getBoardGameByName(@RequestParam String name) {
        List<BoardGame> boardGames = boardGameService.getBoardGameByName(name);
        if (boardGames.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boardGames);
    }

    @PutMapping("/updateBoardGame")
    public ResponseEntity<?> updateBoardGame(@RequestBody BoardGame updatedBoardGame, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
        BoardGame boardGame = boardGameService.updateBoardGame(updatedBoardGame.getGameId(), updatedBoardGame);
        if (boardGame != null) {
            return ResponseEntity.ok(boardGame);
        } else {
            return ResponseEntity.notFound().build();
        }
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/addBoardGame")
    public ResponseEntity<?> addBoardGame(@RequestBody BoardGame boardGame, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
            BoardGame newBoardGame = boardGameService.addBoardGame(boardGame);
            if (newBoardGame != null) {
                return ResponseEntity.ok(newBoardGame);
            } else {
                return ResponseEntity.status(409).body(null);
            }
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }

    }

    @DeleteMapping("/deleteBoardGame")
    public ResponseEntity<?> deleteBoardGame(@RequestParam long gameId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
        boolean deleted = boardGameService.deleteBoardGameByName(gameId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<BoardGame> getBoardGameDetails(@RequestParam long gameId) {
        BoardGame boardGame = boardGameService.getBoardGameDetailsWithReviews(gameId);
        if (boardGame != null) {
            return ResponseEntity.ok(boardGame);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/limited")
    public ResponseEntity<List<BoardGameLimitedProjection>> getLimitedBoardGames(@RequestParam int limit) {
        List<BoardGameLimitedProjection> boardGames = boardGameService.getLimitedBoardGames(limit);
        return ResponseEntity.ok(boardGames);
    }

    @GetMapping("/filterByRating")
    public ResponseEntity<List<BoardGameLimitedProjection>> getBoardGamesByRating(@RequestParam float rating) {
        List<BoardGameLimitedProjection> boardGames = boardGameService.getBoardGamesByRating(rating);
        return ResponseEntity.ok(boardGames);
    }

    @GetMapping("/filterByCategories")
    public ResponseEntity<List<BoardGameLimitedProjection>> getBoardGamesByCategories(@RequestParam List<String> categories) {
        List<BoardGameLimitedProjection> boardGames = boardGameService.getBoardGamesByCategories(categories);
        return ResponseEntity.ok(boardGames);
    }

    @GetMapping("/filterByMechanics")
    public ResponseEntity<List<BoardGameLimitedProjection>> getBoardGamesByMechanics(@RequestParam List<String> mechanics) {
        List<BoardGameLimitedProjection> boardGames = boardGameService.getBoardGamesByMechanics(mechanics);
        return ResponseEntity.ok(boardGames);
    }

    @PutMapping("/updateRatings")
    public ResponseEntity<?> updateAllRatings(HttpSession session, @RequestParam(required = false) String date) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
            reviewService.updateAllBoardGameRatings(date);
            return ResponseEntity.ok("Ratings aggiornati con successo");
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/updateAllReviews")
    public ResponseEntity<?> updateAllReviews(HttpSession session, @RequestParam(required = false) String date) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
            reviewService.updateAllBoardGameReviews(date);
            return ResponseEntity.ok("Recensioni aggiornate con successo per tutti i giochi");
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/updateAllAveragePlayingTime")
    public ResponseEntity<?> updateAllAveragePlayingTime(HttpSession session, @RequestParam(required = false) String date) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.isAdmin()) {
            matchService.updateAllAveragePlayingTime(date);
            return ResponseEntity.ok("AveragePlayingTime aggiornato con successo per tutti i giochi");
        } else {
            return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
        }
    }



}
