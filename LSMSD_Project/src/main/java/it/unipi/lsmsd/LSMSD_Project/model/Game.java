package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Game {
    @Field(name = "id")
    private long gameId;

    private String name;

    public Game(long gameId, String name) {
        this.gameId = gameId;
        this.name = name;
    }

    public Game() {}
}