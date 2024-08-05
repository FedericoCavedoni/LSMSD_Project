package it.unipi.lsmsd.LSMSD_Project.mapper;

import it.unipi.lsmsd.LSMSD_Project.dto.MatchDTO;
import it.unipi.lsmsd.LSMSD_Project.model.Match;

public class MatchMapper {

    public static Match toEntity(MatchDTO matchDTO) {
        Match match = new Match();
        match.setNumPlayers(matchDTO.getNumPlayers());
        match.setResult(matchDTO.isResult());
        match.setDuration(matchDTO.getDuration());
        return match;
    }

    public static MatchDTO toDTO(Match match) {
        MatchDTO matchDTO = new MatchDTO();
        matchDTO.setNumPlayers(match.getNumPlayers());
        matchDTO.setResult(match.isResult());
        matchDTO.setDuration(match.getDuration());
        return matchDTO;
    }
}
