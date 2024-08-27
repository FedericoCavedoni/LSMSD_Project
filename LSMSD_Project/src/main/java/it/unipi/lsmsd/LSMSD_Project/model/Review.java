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
@Document(collection = "Review")
public class Review {
    @Id
    private String mongoId;

    @Field(name = "gameId")
    private long gameId;

    @Field(name = "username")
    private String username;

    @Field(name = "rating")
    private float rating;

    @Field(name = "review text")
    private String reviewText;

    @Field(name = "game")
    private String game;

    @Field(name = "date")
    private String date;
}
