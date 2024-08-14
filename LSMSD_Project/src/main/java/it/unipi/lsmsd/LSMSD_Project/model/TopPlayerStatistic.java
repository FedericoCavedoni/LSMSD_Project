package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopPlayerStatistic {
    private String game;
    private String user;
    private int totalMatches;
    private double winRate;
}