package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.dto.BoardGameDTO;
import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import it.unipi.lsmsd.LSMSD_Project.service.BoardGameService;
import it.unipi.lsmsd.LSMSD_Project.mapper.BoardGameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/boardgames")
public class BoardGameController {

    @Autowired
    private BoardGameService boardGameService;

    @PostMapping("/register")
    public ResponseEntity<BoardGame> registerBoardGame(@RequestBody BoardGameDTO boardGameDTO) {
        BoardGame boardGame = BoardGameMapper.toEntity(boardGameDTO);
        BoardGame newBoardGame = boardGameService.registerNewBoardGame(boardGame);
        return ResponseEntity.ok(newBoardGame);
    }
}
