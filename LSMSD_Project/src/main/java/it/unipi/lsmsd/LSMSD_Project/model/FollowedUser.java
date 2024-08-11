package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FollowedUser {
    private String username;
    private List<String> likedGames;
    private List<String> followedUser;

    public FollowedUser(String username, List<String> likedGames, List<String> followedUser) {
        this.username = username;
        this.likedGames = likedGames;
        this.followedUser = followedUser;
    }
}
