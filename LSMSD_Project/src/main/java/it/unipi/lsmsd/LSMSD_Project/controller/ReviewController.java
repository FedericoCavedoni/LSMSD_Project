package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.dto.ReviewDTO;
import it.unipi.lsmsd.LSMSD_Project.model.Review;
import it.unipi.lsmsd.LSMSD_Project.service.ReviewService;
import it.unipi.lsmsd.LSMSD_Project.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/register")
    public ResponseEntity<Review> registerReview(@RequestBody ReviewDTO reviewDTO) {
        Review review = ReviewMapper.toEntity(reviewDTO);
        Review newReview = reviewService.registerNewReview(review);
        return ResponseEntity.ok(newReview);
    }
}
