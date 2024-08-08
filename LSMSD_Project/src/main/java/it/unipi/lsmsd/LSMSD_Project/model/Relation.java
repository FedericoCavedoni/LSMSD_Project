package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Relation {
    private String firstNode;
    private String relationType;
    private String secondNode;

    public Relation(){};

    public Relation(String firstNode, String relationType, String secondNode) {
        this.firstNode = firstNode;
        this.relationType = relationType;
        this.secondNode = secondNode;
    }
}