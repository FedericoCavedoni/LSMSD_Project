package it.unipi.lsmsd.LSMSD_Project.dao;
import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import it.unipi.lsmsd.LSMSD_Project.model.User;
import it.unipi.lsmsd.LSMSD_Project.model.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode, String> {
    User findByUsername(String username);

    @Query("MATCH (u:User {username: $username})-[r]->(n) " +
            "WHERE $relation IS NULL OR type(r) = $relation " +
            "RETURN u.username AS firstNode, type(r) AS relationType, " +
            "CASE WHEN n:User THEN n.username ELSE n.name END AS secondNode " +
            "LIMIT $limit")
    List<Relation> findUserRelationships(String username, String relation, int limit);
    void deleteByUsername(String username);
}