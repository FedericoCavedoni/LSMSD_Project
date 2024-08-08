package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class RelationshipService {

    @Autowired
    private Neo4jClient neo4jClient;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardGameService boardGameService;

    public List<Relation> getUserRelationships(String username, String relation, int num) {
        return userService.getUserRelationships(username, relation, num);
    }

    public List<Relation> getBoardGameRelationships(String boardGameName, String relation, int num) {
        return boardGameService.getBoardGameRelationships(boardGameName, relation, num);
    }

    @Transactional
    public void followUser(String followerUsername, String followeeUsername) {

        if (userNotExists(followerUsername) || userNotExists(followeeUsername)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        neo4jClient.query("MATCH (f:User {username: $follower}), (e:User {username: $followee}) " +
                        "MERGE (f)-[:FOLLOWED]->(e)")
                .bind(followerUsername).to("follower")
                .bind(followeeUsername).to("followee")
                .run();
    }

    @Transactional
    public void likeBoardGame(String username, String boardGameName) {

        if (userNotExists(username) || boardGameNotExists(boardGameName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or BoardGame not found");
        }

        neo4jClient.query("MATCH (u:User {username: $username}), (b:BoardGame {name: $boardGameName}) " +
                        "MERGE (u)-[:LIKED]->(b)")
                .bind(username).to("username")
                .bind(boardGameName).to("boardGameName")
                .run();
    }

    @Transactional
    public void reviewBoardGame(String username, String boardGameName) {

        if (userNotExists(username) || boardGameNotExists(boardGameName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or BoardGame not found");
        }

        neo4jClient.query("MATCH (u:User {username: $username}), (b:BoardGame {name: $boardGameName}) " +
                        "MERGE (u)-[:REVIEWED]->(b)")
                .bind(username).to("username")
                .bind(boardGameName).to("boardGameName")
                .run();
    }

    @Transactional
    public void addToLibrary(String username, String boardGameName) {

        if (userNotExists(username) || boardGameNotExists(boardGameName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or BoardGame not found");
        }

        neo4jClient.query("MATCH (u:User {username: $username}), (b:BoardGame {name: $boardGameName}) " +
                        "MERGE (u)-[:ADDED_TO_LIBRARY]->(b)")
                .bind(username).to("username")
                .bind(boardGameName).to("boardGameName")
                .run();
    }

    boolean userNotExists(String username){
        System.out.print(username);
        return neo4jClient.query("MATCH (u:User {username: $username}) RETURN u")
                .bind(username).to("username")
                .fetch()
                .one()
                .isEmpty();
    }

    boolean boardGameNotExists(String boardGameName){
        return neo4jClient.query("MATCH (b:BoardGame {name: $name}) RETURN b")
                .bind(boardGameName).to("name")
                .fetch()
                .one()
                .isEmpty();

    }

    @Transactional
    public Relation findRelationship(String firstNode, String relationType, String secondNode) {
        String query = "MATCH (a)-[r:" + relationType + "]-(b) " +
                "WHERE (a.username = $firstNode OR a.name = $firstNode) " +
                "AND (b.username = $secondNode OR b.name = $secondNode) " +
                "RETURN a AS firstNode, type(r) AS relationType, b AS secondNode";

        return neo4jClient.query(query)
                .bind(firstNode).to("firstNode")
                .bind(secondNode).to("secondNode")
                .fetchAs(Relation.class)
                .mappedBy((typeSystem, record) -> {
                    String firstNodeName = record.get("firstNode").asNode().hasLabel("User") ?
                            record.get("firstNode").asNode().get("username").asString(null) :
                            record.get("firstNode").asNode().get("name").asString(null);

                    String secondNodeName = record.get("secondNode").asNode().hasLabel("User") ?
                            record.get("secondNode").asNode().get("username").asString(null) :
                            record.get("secondNode").asNode().get("name").asString(null);

                    return new Relation(firstNodeName, relationType, secondNodeName);
                })
                .one()
                .orElse(null);

    }

    @Transactional
    public void deleteRelationship(String firstNode, String relationType, String secondNode) {
        String query = "MATCH (a)-[r:" + relationType + "]-(b) " +
                "WHERE (a.username = $firstNode OR a.name = $firstNode) " +
                "AND (b.username = $secondNode OR b.name = $secondNode) " +
                "DELETE r";

        neo4jClient.query(query)
                .bind(firstNode).to("firstNode")
                .bind(secondNode).to("secondNode")
                .run();
    }
}


