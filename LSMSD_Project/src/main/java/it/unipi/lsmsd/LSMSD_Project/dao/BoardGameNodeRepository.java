package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.BoardGame;
import it.unipi.lsmsd.LSMSD_Project.model.BoardGameNode;
import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.Optional;

import java.util.List;

public interface BoardGameNodeRepository extends Neo4jRepository<BoardGameNode,Long> {
    BoardGame findByName(String name);

    @Query("MATCH (b:BoardGame {id: $gameId}) DETACH DELETE b")
    void deleteByGameId(Long gameId);

    @Query("MATCH (b:BoardGame {name: $name})-[r]-(n) " +
            "WHERE $relation IS NULL OR type(r) = $relation " +
            "RETURN b.name AS firstNode, type(r) AS relationType, n.username AS secondNode " +
            "LIMIT $limit")
    List<Relation> findBoardGameRelationships(String name, String relation, int limit);
    @Query("MATCH (u:User {username: $username}) " +
            "OPTIONAL MATCH (b:BoardGame {id: $boardGameId}) " +
            "MERGE (u)-[:LIKED]->(b)")
    void likeBoardGame(String username, Long boardGameId);

    @Query("MATCH (b:BoardGame {id: $id}) RETURN b")
    Optional<BoardGameNode> findBoardGameById(Long id);
    @Query("MATCH (a:User {username: $firstNode})-[r:LIKED]->(b:BoardGame {id: $gameId}) " +
            "DELETE r")
    void deleteLikedRelationship(String firstNode, Long gameId);
    @Query("MATCH (u:User {username: $username})-[:FOLLOWS]->(followed:User)-[:LIKED]->(b:BoardGame) " +
            "WHERE NOT (u)-[:LIKED]->(b) " +
            "WITH b, count(b) AS likeCount "+
            "RETURN b.id AS id, b.name AS name " +
            "ORDER BY likeCount DESC " +
            "LIMIT $n")
    List<BoardGameNode> findTopBoardGamesForUser(String username, int n);
    @Query("MATCH (u:User {username: $username})-[:LIKED]->(b:BoardGame) " +
            "RETURN b.id AS id, b.name AS name " +
            "LIMIT $n")
    List<BoardGameNode> findLikedBoardGamesByUsername(String username, int n);
}
