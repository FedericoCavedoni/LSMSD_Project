package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardGameLike {
    private Long id;
    private String name;
    private long like;

    public BoardGameLike(){};

    public BoardGameLike(Long id, String name, long like) {
        this.id=id;
        this.like=like;
        this.name=name;
    }
}