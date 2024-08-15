package it.unipi.lsmsd.LSMSD_Project.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TopAvgPlayersStatistic {
    private String user;
    private Double avgNumGiocatori;
    private Double winRate;
    private Double weightedWinRate;
    private Integer totalMatches;
}
