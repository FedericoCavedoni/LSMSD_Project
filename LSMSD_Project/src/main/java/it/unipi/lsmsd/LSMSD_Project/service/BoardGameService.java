package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.BoardGameRepository;
import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardGameService {

    @Autowired
    private BoardGameRepository boardGameRepository;

    public List<BoardGame> getAllBoardGames() {
        return boardGameRepository.findAll();
    }

    public BoardGame findBoardGameByName(String name) {
        return boardGameRepository.findByName(name);
    }

    public BoardGame updateBoardGame(String name, BoardGame updatedBoardGame) {
        BoardGame existingBoardGame = boardGameRepository.findByName(name);
        if (existingBoardGame != null) {
            existingBoardGame.setName(updatedBoardGame.getName());
            existingBoardGame.setDescription(updatedBoardGame.getDescription());
            existingBoardGame.setCategory(updatedBoardGame.getCategory());
            existingBoardGame.setRating(updatedBoardGame.getRating());
            existingBoardGame.setYear(updatedBoardGame.getYear());
            existingBoardGame.setMinPlayers(updatedBoardGame.getMinPlayers());
            existingBoardGame.setMaxPlayers(updatedBoardGame.getMaxPlayers());
            existingBoardGame.setPlayingTime(updatedBoardGame.getPlayingTime());
            existingBoardGame.setMinAge(updatedBoardGame.getMinAge());
            existingBoardGame.setMechanics(updatedBoardGame.getMechanics());
            existingBoardGame.setDesigners(updatedBoardGame.getDesigners());
            existingBoardGame.setArtists(updatedBoardGame.getArtists());

            return boardGameRepository.save(existingBoardGame);
        }
        return null;
    }

    public BoardGame addBoardGame(BoardGame boardGame) {
        BoardGame existingBoardGame = boardGameRepository.findByName(boardGame.getName());
        if (existingBoardGame == null) {
            return boardGameRepository.save(boardGame);
        }
        return null;
    }

    public boolean deleteBoardGameByName(String name) {
        BoardGame existingBoardGame = boardGameRepository.findByName(name);
        if (existingBoardGame != null) {
            boardGameRepository.delete(existingBoardGame);
            return true;
        }
        return false;
    }
}
