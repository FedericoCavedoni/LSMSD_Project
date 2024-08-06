package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import it.unipi.lsmsd.LSMSD_Project.service.BoardGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/boardgames")
public class BoardGameController {

    @Autowired
    private BoardGameService boardGameService;

    @GetMapping("/getBoardGames")
    public ResponseEntity<List<BoardGame>> getBoardGames() {
        List<BoardGame> boardGames = boardGameService.getAllBoardGames();
        return ResponseEntity.ok(boardGames);
    }

    @GetMapping("/getBoardGameByName")
    public ResponseEntity<BoardGame> getBoardGameByName(@RequestParam String name) {
        BoardGame boardGame = boardGameService.findBoardGameByName(name);
        if (boardGame != null) {
            return ResponseEntity.ok(boardGame);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateBoardGame")
    public ResponseEntity<BoardGame> updateBoardGame(@RequestBody BoardGame updatedBoardGame) {
        BoardGame boardGame = boardGameService.updateBoardGame(updatedBoardGame.getName(), updatedBoardGame);
        if (boardGame != null) {
            return ResponseEntity.ok(boardGame);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addBoardGame")
    public ResponseEntity<BoardGame> addBoardGame(@RequestBody BoardGame boardGame) {
        BoardGame newBoardGame = boardGameService.addBoardGame(boardGame);
        if (newBoardGame != null) {
            return ResponseEntity.ok(newBoardGame);
        } else {
            return ResponseEntity.status(409).body(null); // Conflict status if the game already exists
        }
    }

    @DeleteMapping("/deleteBoardGame")
    public ResponseEntity<Void> deleteBoardGame(@RequestParam String name) {
        boolean deleted = boardGameService.deleteBoardGameByName(name);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
