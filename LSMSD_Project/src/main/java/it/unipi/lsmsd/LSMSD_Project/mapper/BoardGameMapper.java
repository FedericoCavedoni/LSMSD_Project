package it.unipi.lsmsd.LSMSD_Project.mapper;

import it.unipi.lsmsd.LSMSD_Project.dto.BoardGameDTO;
import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;

public class BoardGameMapper {

    public static BoardGame toEntity(BoardGameDTO boardGameDTO) {
        BoardGame boardGame = new BoardGame();
        boardGame.setName(boardGameDTO.getName());
        boardGame.setCategory(boardGameDTO.getCategory());
        boardGame.setRating(boardGameDTO.getRating());
        boardGame.setDescription(boardGameDTO.getDescription());
        return boardGame;
    }

    public static BoardGameDTO toDTO(BoardGame boardGame) {
        BoardGameDTO boardGameDTO = new BoardGameDTO();
        boardGameDTO.setName(boardGame.getName());
        boardGameDTO.setCategory(boardGame.getCategory());
        boardGameDTO.setRating(boardGame.getRating());
        boardGameDTO.setDescription(boardGame.getDescription());
        return boardGameDTO;
    }
}
