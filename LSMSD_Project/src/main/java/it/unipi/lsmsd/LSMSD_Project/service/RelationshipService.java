package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.model.FollowedUser;
import it.unipi.lsmsd.LSMSD_Project.dao.UserNodeRepository;
import it.unipi.lsmsd.LSMSD_Project.dao.BoardGameNodeRepository;
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

        userNodeRepository.followUser(followerUsername, followeeUsername);
    }

    @Transactional
    public void likeBoardGame(String username, Long boardGameId) {
        if (userNotExists(username) || boardGameNotExists(boardGameId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or BoardGame not found");
        }

        boardGameNodeRepository.likeBoardGame(username, boardGameId);
    }


    boolean userNotExists(String username) {
        return userNodeRepository.findUserByUsername(username).isEmpty();
    }

    boolean boardGameNotExists(Long boardGameId) {
        return boardGameNodeRepository.findBoardGameById(boardGameId).isEmpty();
    }

    @Transactional
    public void deleteFollowRelationship(String firstNode, String secondNode) {
        userNodeRepository.deleteFollowRelationship(firstNode, secondNode);
    }

    public void deleteLikedRelationship(String firstNode, Long gameId) {
        boardGameNodeRepository.deleteLikedRelationship(firstNode, gameId);
    }


    @Transactional
    public List<UserNode> getFollowed(String username, int n) {
        return userNodeRepository.findFollowedUsersByUsername(username, n);
    }

    @Transactional
    public List<UserNode> getFollowers(String username, int n) {
        return userNodeRepository.findFollowersByUsername(username, n);
    }

    @Transactional
    public List<BoardGameNode> getTopBoardGamesForUser(String username, int n) {
        return boardGameNodeRepository.findTopBoardGamesForUser(username, n);

    }


    @Transactional
    public List<UserNode> getTopFollowedUsers(String username, int n) {
        return userNodeRepository.findTopFollowedUsers(username, n);

    }

    @Transactional
    public List<BoardGameNode> getLikedBoardGames(String username, int n) {
        return boardGameNodeRepository.findLikedBoardGamesByUsername(username, n);

    }

    @Transactional
    public List<UserSimilarity> getUserMostSimilar(String username, int n) {
        return userNodeRepository.findMostSimilarUsers(username, n);


    }
}