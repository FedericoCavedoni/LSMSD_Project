package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.model.FollowedUser;
import it.unipi.lsmsd.LSMSD_Project.model.BoardGameNode;
import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import it.unipi.lsmsd.LSMSD_Project.model.UserNode;
import it.unipi.lsmsd.LSMSD_Project.model.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collection;
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


    @Transactional
    public void followUser(String followerUsername, String followeeUsername) {
        if (userNotExists(followerUsername) || userNotExists(followeeUsername)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        neo4jClient.query("MATCH (f:User {username: $follower}) " +
                        "MATCH (e:User {username: $followee}) " +
                        "MERGE (f)-[:FOLLOWS]->(e)")
                .bind(followerUsername).to("follower")
                .bind(followeeUsername).to("followee")
                .run();
    }

    @Transactional
    public void likeBoardGame(String username, Long boardGameId) {
        if (userNotExists(username) || boardGameNotExists(boardGameId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or BoardGame not found");
        }

        neo4jClient.query("MATCH (u:User {username: $username}), (b:BoardGame {id: $boardGameId}) " +
                        "MERGE (u)-[:LIKED]->(b)")
                .bind(username).to("username")
                .bind(boardGameId).to("boardGameId")
                .run();
    }


    boolean userNotExists(String username) {
        return neo4jClient.query("MATCH (u:User {username: $username}) RETURN u")
                .bind(username).to("username")
                .fetch()
                .one()
                .isEmpty();
    }

    boolean boardGameNotExists(Long boardGameId) {
        return neo4jClient.query("MATCH (b:BoardGame {id: $id}) RETURN b")
                .bind(boardGameId).to("id")
                .fetch()
                .one()
                .isEmpty();
    }
    @Transactional
    public void deleteFollowRelationship(String firstNode, String secondNode) {
        String query = "MATCH (a:User {username: $firstNode})-[r:FOLLOWS]-(b:User {username: $secondNode}) " +
                "DELETE r";

        neo4jClient.query(query)
                .bind(firstNode).to("firstNode")
                .bind(secondNode).to("secondNode")
                .run();
    }
    @Transactional
    public void deleteLikedRelationship(String firstNode, Long gameId) {
        String query = "MATCH (a:User {username: $firstNode})-[r:LIKED]->(b:BoardGame {id: $gameId}) " +
                "DELETE r";

        neo4jClient.query(query)
                .bind(firstNode).to("firstNode")
                .bind(gameId).to("gameId")
                .run();
    }


    @Transactional
    public List<UserNode> getFollowed(String username, int n) {
        String query = "MATCH (u:User {username: $username})-[:FOLLOWS]->(followed:User) " +
                "RETURN followed.username AS username " +
                "LIMIT $n";

        Collection<Map<String, Object>> result = neo4jClient.query(query)
                .bind(username).to("username")
                .bind(n).to("n")
                .fetch()
                .all();

        List<UserNode> followedUsersList = new ArrayList<>();

        for (Map<String, Object> record : result) {
            String followedUsername = (String) record.get("username");
            followedUsersList.add(new UserNode(followedUsername));
        }

        return followedUsersList;
    }

    @Transactional
    public List<UserNode> getFollowers(String username, int n) {
        String query = "MATCH (u:User {username: $username})<-[:FOLLOWS]-(follower:User) " +
                "RETURN follower.username AS username " +
                "LIMIT $n";

        Collection<Map<String, Object>> result = neo4jClient.query(query)
                .bind(username).to("username")
                .bind(n).to("n")
                .fetch()
                .all();

        List<UserNode> followersList = new ArrayList<>();

        for (Map<String, Object> record : result) {
            String followerUsername = (String) record.get("username");
            followersList.add(new UserNode(followerUsername));
        }

        return followersList;
    }

    //Aiuta a identificare i giochi da tavolo più popolari tra gli utenti seguiti, facilitando il suggerimento di giochi simili.
    @Transactional
    public List<BoardGameNode> getTopBoardGamesForUser(String username, int n) {
        System.out.println(username);
        String query = "MATCH (u:User {username: $username})-[:FOLLOWS]->(followed:User)-[:LIKED]->(b:BoardGame) " +
                "WHERE NOT (u)-[:LIKED]->(b) " +
                "RETURN b.id AS id, b.name AS name, count(b) AS likeCount " +
                "ORDER BY likeCount DESC " +
                "LIMIT $n";

        Collection<Map<String, Object>> result = neo4jClient.query(query)
                .bind(username).to("username")
                .bind(n).to("n")
                .fetch()
                .all();

        List<BoardGameNode> boardGames = new ArrayList<>();

        try {
            for (Map<String, Object> record : result) {
                Long boardGameId = (Long) record.get("id");
                System.out.println(boardGameId);
                String boardGameName = (String) record.get("name");
                System.out.println(boardGameName);
                boardGames.add(new BoardGameNode(boardGameId, boardGameName));
                System.out.println(boardGames);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
        }

        return boardGames;
    }


    //restituisce una lista di utenti che sono maggiormente seguiti dagli utenti che un determinato utente sta seguendo, ordinati per numero di follower decrescente.
    @Transactional
    public List<UserNode> getTopFollowedUsers(String username, int n) {
        String query = "MATCH (u:User {username: $username})-[:FOLLOWS]->(followed:User)-[:FOLLOWS]->(target:User) " +
                "RETURN target.username AS followedUser, count(target) AS followCount " +
                "ORDER BY followCount DESC " +
                "LIMIT $n";

        Collection<Map<String, Object>> result = neo4jClient.query(query)
                .bind(username).to("username")
                .bind(n).to("n")
                .fetch()
                .all();

        List<UserNode> followedUsers = new ArrayList<>();
        System.out.println("Fetched result size: " + result.size());

        for (Map<String, Object> record : result) {
            String followedUser = (String) record.get("followedUser");
            followedUsers.add(new UserNode(followedUser));
        }

        return followedUsers;
    }

    @Transactional
    public List<BoardGameNode> getLikedBoardGames(String username, int n) {
        System.out.println("Executing query with username: " + username + " and limit: " + n);
        String query = "MATCH (u:User {username: $username})-[:LIKED]->(b:BoardGame) " +
                "RETURN b.id AS id, b.name AS name " +
                "LIMIT $n";

        Collection<Map<String, Object>> result = neo4jClient.query(query)
                .bind(username).to("username")
                .bind(n).to("n")
                .fetch()
                .all();

        List<BoardGameNode> likedBoardGamesList = new ArrayList<>();
        System.out.println("Fetched result size: " + result.size());

        try {
            for (Map<String, Object> record : result) {
                Long boardGameId = (Long) record.get("id");
                System.out.println(boardGameId);
                String boardGameName = (String) record.get("name");
                System.out.println(boardGameName);
                likedBoardGamesList.add(new BoardGameNode(boardGameId, boardGameName));
                System.out.println(likedBoardGamesList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
        }

        return likedBoardGamesList;
    }
    @Transactional
    public List<UserSimilarity> getUserMostSimilar(String username, int n) {
        System.out.println("Executing query with username: " + username + " and limit: " + n);
        String query = "MATCH (u:User {username: $username})-[:LIKED]->(b:BoardGame)<-[:LIKED]-(other:User) " +
                "WHERE u <> other " +
                "WITH other, count(b) AS commonGames " +
                "ORDER BY commonGames DESC " +
                "RETURN other.username AS username, commonGames " +
                "LIMIT $n";

        Collection<Map<String, Object>> result = neo4jClient.query(query)
                .bind(username).to("username")
                .bind(n).to("n")
                .fetch()
                .all();
        System.out.println("Fetched result size: " + result.size());

        List<UserSimilarity> similarUsersList = new ArrayList<>();
        try {
        for (Map<String, Object> record : result) {
            String similarUserName = (String) record.get("username");
            long commonGames = (long) record.get("commonGames");
            similarUsersList.add(new UserSimilarity(similarUserName, commonGames));
        }}catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
        }


        return similarUsersList;
    }

}