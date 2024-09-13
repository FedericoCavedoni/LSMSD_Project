package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.BoardGameRepository;
import it.unipi.lsmsd.LSMSD_Project.dao.ReviewRepository;
import it.unipi.lsmsd.LSMSD_Project.model.*;
import it.unipi.lsmsd.LSMSD_Project.projections.BoardGameLimitedProjection;
import it.unipi.lsmsd.LSMSD_Project.dao.BoardGameNodeRepository;
import it.unipi.lsmsd.LSMSD_Project.projections.BoardGameNameProjection;
import it.unipi.lsmsd.LSMSD_Project.projections.ReviewProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardGameService {

    @Autowired
    private BoardGameRepository boardGameRepository;

    @Autowired
    private BoardGameNodeRepository boardGameNodeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<BoardGame> getAllBoardGames() {
        return boardGameRepository.findAll();
    }



    public List<BoardGameLimitedProjection> getLimitedBoardGameByName(String name) {
        return boardGameRepository.findLimitedBoardGameByName(name);
    }


    public BoardGame updateBoardGame(long id, BoardGame updatedBoardGame) {
        BoardGame existingBoardGame = boardGameRepository.findByGameId(id);
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
            existingBoardGame.setReviews(updatedBoardGame.getReviews());

            BoardGame savedBoardGame = boardGameRepository.save(existingBoardGame);

            BoardGameNode boardGameNode = new BoardGameNode();
            boardGameNode.setId(savedBoardGame.getGameId());
            boardGameNode.setName(savedBoardGame.getName());
            boardGameNodeRepository.save(boardGameNode);

            return savedBoardGame;
        }
        return null;
    }

    public BoardGame addBoardGame(BoardGame boardGame) {
        BoardGame existingBoardGame = boardGameRepository.findByGameId(boardGame.getGameId());
        if (existingBoardGame == null) {
            BoardGame savedBoardGame = boardGameRepository.save(boardGame);

            BoardGameNode boardGameNode = new BoardGameNode(savedBoardGame.getGameId(), savedBoardGame.getName());
            boardGameNodeRepository.save(boardGameNode);

            return savedBoardGame;
        }
        return null;
    }

    public boolean deleteBoardGameByName(long gameId) {
        BoardGame existingBoardGame = boardGameRepository.findByGameId(gameId);
        if (existingBoardGame != null) {
            boardGameRepository.delete(existingBoardGame);
            boardGameNodeRepository.deleteByGameId(gameId);

            return true;
        }
        return false;
    }


    public BoardGame getBoardGameDetailsWithReviews(long gameId) {
        BoardGame boardGame = boardGameRepository.findByGameId(gameId);
        if (boardGame != null) {
            // Recupera le recensioni usando la proiezione
            List<ReviewProjection> reviews = reviewRepository.findReviewsByGameId(gameId);

            // Converte le recensioni in oggetti FilteredReview
            List<FilteredReview> filteredReviews = reviews.stream()
                    .filter(r -> r.getUsername() != null && r.getRating() != null) // Filtra eventuali campi nulli
                    .map(r -> new FilteredReview(r.getUsername(), r.getRating(), r.getReviewText()))
                    .collect(Collectors.toList());

            // Imposta le recensioni filtrate nel boardGame (se necessario puoi aggiungere un setter per filteredReviews)
            boardGame.setFilteredReviews(filteredReviews); // Questo richiede che BoardGame abbia un campo di tipo List<FilteredReview>.

            return boardGame;
        }
        return null;
    }





    public List<BoardGameLimitedProjection> getLimitedBoardGames(int limit) {
        return boardGameRepository.findLimitedBoardGames().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<BoardGameLimitedProjection> getBoardGamesByRating(float rating) {
        return boardGameRepository.findBoardGamesWithRatingGreaterThanEqual(rating);
    }

    public List<BoardGameLimitedProjection> getBoardGamesByCategories(List<String> categories) {
        return boardGameRepository.findBoardGamesByCategories(categories);
    }

    public List<BoardGameLimitedProjection> getBoardGamesByMechanics(List<String> mechanics) {
        return boardGameRepository.findBoardGamesByMechanics(mechanics);
    }

    public BoardGame getBoardGameByGameId(Long gameId) {
        return boardGameRepository.findByGameId(gameId);
    }


}
