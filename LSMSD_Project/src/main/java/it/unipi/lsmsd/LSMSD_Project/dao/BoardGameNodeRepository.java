package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import it.unipi.lsmsd.LSMSD_Project.model.BoardGameNode;
import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Map;

public interface BoardGameNodeRepository extends Neo4jRepository<BoardGameNode, String> {
    BoardGame findByName(String name);

    @Query("MATCH (b:BoardGame {name: $name}) DETACH DELETE b")
    void deleteByName(String name);

    @Query("MATCH (b:BoardGame {name: $name})-[r]-(n) " +
            "WHERE $relation IS NULL OR type(r) = $relation " +
            "RETURN b.name AS firstNode, type(r) AS relationType, n.username AS secondNode " +
            "LIMIT $limit")
    List<Relation> findBoardGameRelationships(String name, String relation, int limit);
}
