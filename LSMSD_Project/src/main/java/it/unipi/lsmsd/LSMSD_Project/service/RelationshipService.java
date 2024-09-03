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
    @Autowired
    private UserNodeRepository userNodeRepository;

    @Autowired
    private BoardGameNodeRepository boardGameNodeRepository;


    @Transactional
    public void followUser(String followerUsername, String followeeUsername) {
        if (userNotExists(followerUsername) || userNotExists(followeeUsername)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userNodeRepository.followUser(followerUsername, followeeUsername); // Use a custom query if needed
    }

    @Transactional
    public void likeBoardGame(String username, Long boardGameId) {
        if (userNotExists(username) || boardGameNotExists(boardGameId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or BoardGame not found");
        }

        boardGameNodeRepository.likeBoardGame(username, boardGameId); // Use a custom query if needed
    }


    boolean userNotExists(String username) {
        return userNodeRepository.findUserByUsername(username).isEmpty();
    }

    boolean boardGameNotExists(Long boardGameId) {
        return boardGameNodeRepository.findBoardGameById(boardGameId).isEmpty();
    }
    @Transactional
    public void deleteFollowRelationship(String firstNode, String secondNode) {
        userNodeRepository.deleteFollowRelationship(firstNode, secondNode); // Use a custom query if needed
    }
    public void deleteLikedRelationship(String firstNode, Long gameId) {
        boardGameNodeRepository.deleteLikedRelationship(firstNode, gameId); // Use a custom query if needed
    }



    @Transactional
    public List<UserNode> getFollowed(String username, int n) {
        List<Map<String, Object>> result = userNodeRepository.findFollowedUsersByUsername(username, n);

        List<UserNode> followedUsersList = new ArrayList<>();
        for (Map<String, Object> record : result) {
            String followedUsername = (String) record.get("username");
            followedUsersList.add(new UserNode(followedUsername));
        }

        return followedUsersList;
    }

    @Transactional
    public List<UserNode> getFollowers(String username, int n) {
        List<Map<String, Object>> result = userNodeRepository.findFollowersByUsername(username, n);

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
        List<Map<String, Object>> result = boardGameNodeRepository.findTopBoardGamesForUser(username, n);

        List<BoardGameNode> boardGames = new ArrayList<>();

        try {
            for (Map<String, Object> record : result) {
                Long boardGameId = (Long) record.get("id");
                String boardGameName = (String) record.get("name");
                boardGames.add(new BoardGameNode(boardGameId, boardGameName));
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
        List<Map<String, Object>> result = userNodeRepository.findTopFollowedUsers(username, n);

        List<UserNode> followedUsers = new ArrayList<>();

        for (Map<String, Object> record : result) {
            String followedUser = (String) record.get("followedUser");
            followedUsers.add(new UserNode(followedUser));
        }

        return followedUsers;
    }
    @Transactional
    public List<BoardGameNode> getLikedBoardGames(String username, int n) {
        System.out.println("Executing query with username: " + username + " and limit: " + n);
        List<Map<String, Object>> result = boardGameNodeRepository.findLikedBoardGamesByUsername(username, n);

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
        List<Map<String, Object>> result = userNodeRepository.findMostSimilarUsers(username, n);

        List<UserSimilarity> similarUsersList = new ArrayList<>();
        System.out.println("Fetched result size: " + result.size());

        try {
            for (Map<String, Object> record : result) {
                String similarUserName = (String) record.get("username");
                long commonGames = (long) record.get("commonGames");
                similarUsersList.add(new UserSimilarity(similarUserName, commonGames));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
        }

        return similarUsersList;
    }

}