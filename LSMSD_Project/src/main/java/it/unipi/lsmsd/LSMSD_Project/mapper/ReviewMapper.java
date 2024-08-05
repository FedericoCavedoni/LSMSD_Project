package it.unipi.lsmsd.LSMSD_Project.mapper;

import it.unipi.lsmsd.LSMSD_Project.dto.ReviewDTO;
import it.unipi.lsmsd.LSMSD_Project.model.Review;

public class ReviewMapper {

    public static Review toEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setRate(reviewDTO.getRate());
        review.setComment(reviewDTO.getComment());
        review.setDate(reviewDTO.getDate());
        return review;
    }

    public static ReviewDTO toDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRate(review.getRate());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setDate(review.getDate());
        return reviewDTO;
    }
}
