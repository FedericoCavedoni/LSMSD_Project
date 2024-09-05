package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;

@Data
public class TopGameStatistic {
    private String game;
    private long totalMatches;
    private long totalTimePlayed;
}