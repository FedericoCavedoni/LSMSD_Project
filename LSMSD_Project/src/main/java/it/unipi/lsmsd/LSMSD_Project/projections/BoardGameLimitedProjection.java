package it.unipi.lsmsd.LSMSD_Project.projections;

import java.util.List;

public interface BoardGameLimitedProjection {
    String getName();
    List<String> getCategory();
    int getMinPlayers();
    int getMaxPlayers();
    float getRating();
    int getPlayingTime();
    List<String> getReviews();
}