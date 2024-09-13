package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FollowedUser {
    private String username;
    private long follower;

    public FollowedUser(String username, long follower) {
        this.username = username;
        this.follower = follower;
    }
}
