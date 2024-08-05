package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.BoardGameRepository;
import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardGameService {

    @Autowired
    private BoardGameRepository boardGameRepository;

    public BoardGame registerNewBoardGame(BoardGame boardGame) {
        return boardGameRepository.save(boardGame);
    }
}
