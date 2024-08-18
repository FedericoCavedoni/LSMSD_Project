package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSimilarity {
    private String username;
    private long commonGames;

    public UserSimilarity(String username, long commonGames) {
        this.username = username;
        this.commonGames = commonGames;
    }
}