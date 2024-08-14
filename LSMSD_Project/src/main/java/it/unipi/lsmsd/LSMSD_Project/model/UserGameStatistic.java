package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;

@Data
public class UserGameStatistic {
    private String user;
    private int totalMatches;
    private double winRate;
    private String mostPlayedGame;
    private String leastPlayedGame;
}