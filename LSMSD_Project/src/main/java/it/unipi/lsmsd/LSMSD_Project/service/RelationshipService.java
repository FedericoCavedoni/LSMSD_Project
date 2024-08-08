package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Service
public class RelationshipService {

    @Autowired
    private Neo4jClient neo4jClient;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardGameService boardGameService;

    public List<Relation> getUserRelationships(String username, String relation) {
        return userService.getUserRelationships(username, relation);
    }

    public List<Relation> getBoardGameRelationships(String boardGameName, String relation) {
        return boardGameService.getBoardGameRelationships(boardGameName, relation);
    }

    @Transactional
    public void followUser(String followerUsername, String followeeUsername) {

        if (!userExists(followerUsername) || !boardGameExists(followeeUsername)) {
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

        if (!userExists(username) || !boardGameExists(boardGameName)) {
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

        if (!userExists(username) || !boardGameExists(boardGameName)) {
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

        if (!userExists(username) || !boardGameExists(boardGameName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or BoardGame not found");
        }

        neo4jClient.query("MATCH (u:User {username: $username}), (b:BoardGame {name: $boardGameName}) " +
                        "MERGE (u)-[:ADDED_TO_LIBRARY]->(b)")
                .bind(username).to("username")
                .bind(boardGameName).to("boardGameName")
                .run();
    }

    boolean userExists(String username){
        return neo4jClient.query("MATCH (u:User {username: $username}) RETURN u")
                .bind(username).to("username")
                .fetch()
                .one()
                .isPresent();
    }

    boolean boardGameExists(String boardGameName){
        return neo4jClient.query("MATCH (b:BoardGame {name: $name}) RETURN b")
                .bind(boardGameName).to("name")
                .fetch()
                .one()
                .isPresent();

    }
}
