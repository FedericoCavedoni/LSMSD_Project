package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.model.FollowedUser;
import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import it.unipi.lsmsd.LSMSD_Project.model.User;
import it.unipi.lsmsd.LSMSD_Project.service.RelationshipService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/relation")
public class RelationshipController {

    @Autowired
    private RelationshipService relationshipService;

    @GetMapping("/user")
    public ResponseEntity<List<Relation>> getUserRelationships(@RequestParam String username, @RequestParam(required = false) String relation, @RequestParam int n) {
        List<Relation> relationships = relationshipService.getUserRelationships(username, relation, n);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/boardgame")
    public ResponseEntity<List<Relation>> getBoardGameRelationships(@RequestParam String name, @RequestParam(required = false) String relation, @RequestParam int n) {
        List<Relation> relationships = relationshipService.getBoardGameRelationships(name, relation, n);
        return ResponseEntity.ok(relationships);
    }

    @PostMapping("/addFollow")
    public ResponseEntity<?> followUser(@RequestParam String followee,HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        try {
            relationshipService.followUser(currentUser.getUsername(), followee);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @PostMapping("/addLike")
    public ResponseEntity<?> likeBoardGame(@RequestParam String boardGameName,HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        try {
            relationshipService.likeBoardGame(currentUser.getUsername(), boardGameName);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @GetMapping("/find")
    public ResponseEntity<Relation> findRelationship(
            @RequestParam String firstNode,
            @RequestParam String relationType,
            @RequestParam String secondNode) {

        Relation relation = relationshipService.findRelationship(firstNode, relationType, secondNode);
        if (relation != null) {
            return ResponseEntity.ok(relation);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteRelationship(
            @RequestParam(required = false) String firstNode,
            @RequestParam String relationType,
            @RequestParam String secondNode) {

        try {
            relationshipService.deleteRelationship(firstNode, relationType, secondNode);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/followedAndLikedGames")
    public ResponseEntity<List<FollowedUser>> getFollowedUsersAndLikedGames(@RequestParam String username, @RequestParam int n) {
        try {
            List<FollowedUser> followedUsers = relationshipService.getFollowedUsersAndLikedGames(username, n);
            return new ResponseEntity<>(followedUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/followed")
    public ResponseEntity<List<FollowedUser>> getFollowed(@RequestParam String username, @RequestParam int n) {
        try {
            List<FollowedUser> followedUsers = relationshipService.getFollowed(username, n);
            return new ResponseEntity<>(followedUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/topFollowedUsers")
    public ResponseEntity<List<String>> getTopFollowedUsers(@RequestParam String username, @RequestParam int n) {
        try {
            List<String> followedUsers = relationshipService.getTopFollowedUsers(username, n);
            return new ResponseEntity<>(followedUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/topBoardGames")
    public ResponseEntity<List<String>> getTopBoardGames(@RequestParam String username, @RequestParam int n) {
        try {
            List<String> boardGames = relationshipService.getTopBoardGamesForUser(username, n);
            return new ResponseEntity<>(boardGames, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
