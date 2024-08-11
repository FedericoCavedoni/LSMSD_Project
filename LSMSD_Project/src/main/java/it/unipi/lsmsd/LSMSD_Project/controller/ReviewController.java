package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.model.Review;
import it.unipi.lsmsd.LSMSD_Project.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        Review newReview = reviewService.addReview(review);
        if (newReview != null) {
            return ResponseEntity.ok(newReview);
        } else {
            return ResponseEntity.status(409).body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReview(@RequestParam String username, @RequestParam String game) {
        boolean deleted = reviewService.deleteReview(username, game);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getByUsername")
    public ResponseEntity<List<Review>> getReviewsByUsername(@RequestParam String username) {
        List<Review> reviews = reviewService.getReviewsByUsername(username);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/getByGame")
    public ResponseEntity<List<Review>> getReviewsByGame(@RequestParam String game) {
        List<Review> reviews = reviewService.getReviewsByGame(game);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/averageRating")
    public ResponseEntity<Double> getAverageRatingByGame(@RequestParam String game) {
        double averageRating = reviewService.getAverageRatingByGame(game);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/findReview")
    public ResponseEntity<Review> getReviewByUserAndGame(@RequestParam String username, @RequestParam String game) {
        List<Review> reviews = reviewService.findReviewByUserAndGame(username, game);

        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (reviews.size() > 1) {
            return ResponseEntity.status(500).body(null);
        } else {
            return ResponseEntity.ok(reviews.get(0));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Review>> getRecentReviews(@RequestParam String game, int num) {
        try {
            List<Review> recentReviews = reviewService.getRecentReviews(game, num);
            return new ResponseEntity<>(recentReviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
