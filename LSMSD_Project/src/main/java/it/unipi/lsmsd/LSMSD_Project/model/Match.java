package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@Data
@Document(collection = "Match")
public class Match {

    @Id
    private String mongoid;
    @Field(name = "gameId")
    private long gameId;

    @Field(name = "user")
    private String user;

    @Field(name = "game")
    private String game;

    @Field(name = "numgiocatori")
    private int numGiocatori;

    @Field(name = "result")
    private boolean result;

    @Field(name = "duration")
    private int duration;

    @Field(name = "date")
    private String date;
}
