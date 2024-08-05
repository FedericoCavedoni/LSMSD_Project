package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.MatchRepository;
import it.unipi.lsmsd.LSMSD_Project.model.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public Match registerNewMatch(Match match) {
        return matchRepository.save(match);
    }
}
