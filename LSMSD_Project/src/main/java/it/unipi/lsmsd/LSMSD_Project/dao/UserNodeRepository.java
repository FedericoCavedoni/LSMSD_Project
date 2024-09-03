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

    @Query("MATCH (u:User {username: $username}) RETURN u")
    Optional<UserNode> findUserByUsername(String username);

    @Query("MATCH (u:User {username: $username})-[r]->(n) " +
            "WHERE $relation IS NULL OR type(r) = $relation " +
            "RETURN u.username AS firstNode, type(r) AS relationType, " +
            "CASE WHEN n:User THEN n.username ELSE n.name END AS secondNode " +
            "LIMIT $limit")
    List<Relation> findUserRelationships(String username, String relation, int limit);
    @Query("MATCH (f:User {username: $followerUsername}) " +
            "MATCH (e:User {username: $followeeUsername}) " +
            "MERGE (f)-[:FOLLOWS]->(e)")
    void followUser(String followerUsername, String followeeUsername);
    @Query("MATCH (a:User {username: $firstNode})-[r:FOLLOWS]->(b:User {username: $secondNode}) " +
            "DELETE r")
    void deleteFollowRelationship(String firstNode, String secondNode);
    @Query("MATCH (u:User {username: $username})-[:FOLLOWS]->(followed:User) " +
            "RETURN followed.username AS username " +
            "LIMIT $n")
    List<Map<String, Object>> findFollowedUsersByUsername(String username, int n);
    @Query("MATCH (u:User {username: $username})<-[:FOLLOWS]-(follower:User) " +
            "RETURN follower.username AS username " +
            "LIMIT $n")
    List<Map<String, Object>> findFollowersByUsername(String username, int n);
    @Query("MATCH (u:User {username: $username})-[:FOLLOWS]->(followed:User)-[:FOLLOWS]->(target:User) " +
            "RETURN target.username AS followedUser, count(target) AS followCount " +
            "ORDER BY followCount DESC " +
            "LIMIT $n")
    List<Map<String, Object>> findTopFollowedUsers(String username, int n);
    @Query("MATCH (u:User {username: $username})-[:LIKED]->(b:BoardGame)<-[:LIKED]-(other:User) " +
            "WHERE u <> other " +
            "WITH other, count(b) AS commonGames " +
            "ORDER BY commonGames DESC " +
            "RETURN other.username AS username, commonGames " +
            "LIMIT $n")
    List<Map<String, Object>> findMostSimilarUsers(String username, int n);

    void deleteByUsername(String username);
}