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

    @PostMapping("/register")
    public ResponseEntity<BoardGame> registerBoardGame(@RequestBody BoardGame boardGame) {
        BoardGame newBoardGame = boardGameService.registerNewBoardGame(boardGame);
        return ResponseEntity.ok(newBoardGame);
    }

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
}
