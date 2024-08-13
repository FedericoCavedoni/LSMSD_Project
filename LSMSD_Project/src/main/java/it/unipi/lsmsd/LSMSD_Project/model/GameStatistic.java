package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;

@Data
public class GameStatistic {

    private String game;
    private int totalMatches;
    private int totalWins;
    private double avgDuration;
    private double winRate;
}
