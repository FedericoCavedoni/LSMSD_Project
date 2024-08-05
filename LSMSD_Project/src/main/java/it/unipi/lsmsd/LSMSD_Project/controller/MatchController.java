package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.dto.MatchDTO;
import it.unipi.lsmsd.LSMSD_Project.model.Match;
import it.unipi.lsmsd.LSMSD_Project.service.MatchService;
import it.unipi.lsmsd.LSMSD_Project.mapper.MatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/register")
    public ResponseEntity<Match> registerMatch(@RequestBody MatchDTO matchDTO) {
        Match match = MatchMapper.toEntity(matchDTO);
        Match newMatch = matchService.registerNewMatch(match);
        return ResponseEntity.ok(newMatch);
    }
}
