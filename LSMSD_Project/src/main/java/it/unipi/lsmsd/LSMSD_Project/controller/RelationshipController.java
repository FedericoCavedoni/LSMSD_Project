package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.model.*;
import it.unipi.lsmsd.LSMSD_Project.service.RelationshipService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/relation")
public class RelationshipController {

    @Autowired
    private RelationshipService relationshipService;


    @PostMapping("/addFollow")
    public ResponseEntity<?> followUser(@RequestParam String followee, HttpSession session) {
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
    public ResponseEntity<?> likeBoardGame(@RequestParam Long boardGameId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        try {
            relationshipService.likeBoardGame(currentUser.getUsername(), boardGameId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }


    @DeleteMapping("/deletefollow")
    public ResponseEntity<?> deleteFollowRelationship(
            @RequestParam(required = false) String firstNode,
            @RequestParam String secondNode,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }

        if (firstNode == null) {
            firstNode = currentUser.getUsername();
        } else {
            if (!currentUser.isAdmin()) {
                return new ResponseEntity<>("User not authorized", HttpStatus.UNAUTHORIZED);
            }
        }

        try {
            relationshipService.deleteFollowRelationship(firstNode, secondNode);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/deletelike")
    public ResponseEntity<?> deleteLikedRelationship(
            @RequestParam(required = false) String firstNode,
            @RequestParam Long gameId,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }

        if (firstNode == null) {
            firstNode = currentUser.getUsername();
        } else {
            if (!currentUser.isAdmin()) {
                return new ResponseEntity<>("User not authorized", HttpStatus.UNAUTHORIZED);
            }
        }

        try {
            relationshipService.deleteLikedRelationship(firstNode, gameId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/followed")
    public ResponseEntity<?> getFollowed(@RequestParam(required = false) String username, @RequestParam int n, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        if (username == null) {
            username = currentUser.getUsername();
        }
        try {
            List<UserNode> followedUsers = relationshipService.getFollowed(username, n);
            return new ResponseEntity<>(followedUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(@RequestParam(required = false) String username, @RequestParam int n, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        if (username == null) {
            username = currentUser.getUsername();
        }

        try {
            List<UserNode> followers = relationshipService.getFollowers(username, n);
            return new ResponseEntity<>(followers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/likedGames")
    public ResponseEntity<?> getLikedBoardGames(
            @RequestParam(required = false) String username,
            @RequestParam int n,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        if (username == null) {
            username = currentUser.getUsername();
        }

        try {
            List<BoardGameNode> likedBoardGames = relationshipService.getLikedBoardGames(username, n);
            return new ResponseEntity<>(likedBoardGames, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/topFollowedUsers")
    public ResponseEntity<?> getTopFollowedUsers(@RequestParam int n, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        try {
            List<UserNode> followedUsers = relationshipService.getTopFollowedUsers(currentUser.getUsername(), n);
            return new ResponseEntity<>(followedUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/topBoardGames")
    public ResponseEntity<?> getTopBoardGames(
            @RequestParam int n,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        try {
            List<BoardGameNode> boardGames = relationshipService.getTopBoardGamesForUser(currentUser.getUsername(), n);
            return new ResponseEntity<>(boardGames, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userMostSimilar")
    public ResponseEntity<?> userMostSimilar(@RequestParam int n, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        try {
            List<UserSimilarity> similarUsers = relationshipService.getUserMostSimilar(currentUser.getUsername(), n);
            return new ResponseEntity<>(similarUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/topUsers")
    public ResponseEntity<?> getTopUsers(@RequestParam int n, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        try {
            List<FollowedUser> topFollowedUsers = relationshipService.getTopUsers(n);
            return ResponseEntity.ok(topFollowedUsers);
        } catch (Exception e) {
            e.printStackTrace();  // or use a proper logging framework

            // Return a detailed error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal server error occurred: " + e.getMessage());
        }
    }


    // Endpoint per ottenere i giochi più apprezzati
    @GetMapping("/topLikedBoardGames")
    public ResponseEntity<?> getTopLikedBoardGames(@RequestParam int n, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        try {
            List<BoardGameLike> topLikedBoardGames = relationshipService.getTopLikedBoardGames(n);
            return ResponseEntity.ok(topLikedBoardGames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}