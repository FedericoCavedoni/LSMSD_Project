package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.BoardGameNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface BoardGameNodeRepository extends Neo4jRepository<BoardGameNode, String> {
    @Query("MATCH (b:BoardGame {name: $name}) DETACH DELETE b")
    void deleteByName(String name);
}
