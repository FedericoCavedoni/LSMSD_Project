package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import it.unipi.lsmsd.LSMSD_Project.service.RelationshipService;
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
        List<Relation> relationships = relationshipService.getUserRelationships(username, relation);

        List<Relation> selectedRelationships;

        if (relationships.size() > n) {
            selectedRelationships = relationships.subList(0, n);
        } else {
            selectedRelationships = relationships;
        }

        return ResponseEntity.ok(selectedRelationships);
    }

    @GetMapping("/boardgame")
    public ResponseEntity<List<Relation>> getBoardGameRelationships(@RequestParam String name,  @RequestParam(required = false) String relation, @RequestParam int n) {
        List<Relation> relationships = relationshipService.getBoardGameRelationships(name, relation);

        List<Relation> selectedRelationships;

        if (relationships.size() > n) {
            selectedRelationships = relationships.subList(0, n);
        } else {
            selectedRelationships = relationships;
        }

        return ResponseEntity.ok(selectedRelationships);
    }

    @PostMapping("/addFollow")
    public void followUser(@RequestParam String follower, @RequestParam String followee) {
        try {
            relationshipService.followUser(follower, followee);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @PostMapping("/addLike")
    public void likeBoardGame(@RequestParam String username, @RequestParam String boardGameName) {
        try {
            relationshipService.likeBoardGame(username, boardGameName);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @PostMapping("/addReview")
    public void reviewBoardGame(@RequestParam String username, @RequestParam String boardGameName) {
        try {
            relationshipService.reviewBoardGame(username, boardGameName);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @PostMapping("/addLibrary")
    public void addToLibrary(@RequestParam String username, @RequestParam String boardGameName) {
        try {
            relationshipService.addToLibrary(username, boardGameName);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
}
