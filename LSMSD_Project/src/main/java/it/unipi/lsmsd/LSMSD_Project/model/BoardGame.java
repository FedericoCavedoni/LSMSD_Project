package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Setter
@Getter
@Data
@Document(collection = "BoardGames")
public class BoardGame {
    @Id
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "boardgamecategory")
    private List<String> category;

    @Field(name = "rating")
    private float rating;

    @Field(name = "Year")
    private int year;

    @Field(name = "minplayers")
    private int minPlayers;

    @Field(name = "maxplayers")
    private int maxPlayers;

    @Field(name = "playingtime")
    private int playingTime;

    @Field(name = "minage")
    private int minAge;

    @Field(name = "boardgamemechanic")
    private List<String> mechanics;

    @Field(name = "boardgamedesigner")
    private List<String> designers;

    @Field(name = "boardgameartist")
    private List<String> artists;

    @Field(name = "reviews")
    private List<Review> reviews;

    @Field(name = "averageplayingtime")
    private double averagePlayingTime;
}
