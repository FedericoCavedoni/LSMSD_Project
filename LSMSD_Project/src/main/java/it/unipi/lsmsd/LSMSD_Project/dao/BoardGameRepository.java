package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardGameRepository extends MongoRepository<BoardGame, String> {
}
