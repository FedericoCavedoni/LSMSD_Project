package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.BoardGameRepository;
import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import it.unipi.lsmsd.LSMSD_Project.model.BoardGameNode;
import it.unipi.lsmsd.LSMSD_Project.dao.BoardGameNodeRepository;
import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardGameService {

    @Autowired
    private BoardGameRepository boardGameRepository;

    @Autowired
    private BoardGameNodeRepository boardGameNodeRepository;

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

            BoardGame savedBoardGame = boardGameRepository.save(existingBoardGame);

            BoardGameNode boardGameNode = new BoardGameNode();
            boardGameNode.setId(savedBoardGame.getId());
            boardGameNode.setName(savedBoardGame.getName());
            boardGameNodeRepository.save(boardGameNode);

            return savedBoardGame;
        }
        return null;
    }

    public BoardGame addBoardGame(BoardGame boardGame) {
        BoardGame existingBoardGame = boardGameRepository.findByName(boardGame.getName());
        if (existingBoardGame == null) {
            BoardGame savedBoardGame = boardGameRepository.save(boardGame);

            BoardGameNode boardGameNode = new BoardGameNode(savedBoardGame.getId(), savedBoardGame.getName());
            boardGameNodeRepository.save(boardGameNode);

            return savedBoardGame;
        }
        return null;
    }

    public boolean deleteBoardGameByName(String name) {
        BoardGame existingBoardGame = boardGameRepository.findByName(name);
        if (existingBoardGame != null) {
            boardGameRepository.delete(existingBoardGame);
            boardGameNodeRepository.deleteByName(name);

            return true;
        }
        return false;
    }

    public List<Relation> getBoardGameRelationships(String boardGameName, String relation, int num) {
        return boardGameNodeRepository.findBoardGameRelationships(boardGameName, relation, num);
    }

}
