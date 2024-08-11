package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FollowedUser {
    private String username;
    private List<String> likedGames;

    public FollowedUser(String username, List<String> likedGames) {
        this.username = username;
        this.likedGames = likedGames;
    }
}
