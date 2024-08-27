package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;

@Data
public class TopGameStatistic {
    private String game;
    private long totalMatches;  // Solo per il numero di partite giocate
    private long totalTimePlayed;  // Solo per il tempo totale giocato
}