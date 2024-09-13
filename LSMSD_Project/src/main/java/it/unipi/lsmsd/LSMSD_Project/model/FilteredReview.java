package it.unipi.lsmsd.LSMSD_Project.model;

public class FilteredReview {
    private String username;
    private Float rating;
    private String reviewText;

    public FilteredReview(String username, Float rating, String reviewText) {
        this.username = username;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
