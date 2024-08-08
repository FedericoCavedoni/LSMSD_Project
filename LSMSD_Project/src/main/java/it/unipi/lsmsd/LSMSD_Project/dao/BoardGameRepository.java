package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;

public interface BoardGameRepository extends MongoRepository<BoardGame, String> {
    BoardGame findByName(String name);
}
