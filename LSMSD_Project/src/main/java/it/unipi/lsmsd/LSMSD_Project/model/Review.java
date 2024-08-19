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
    private String mongoId;  // Questo è il campo che rappresenta l'_id generato automaticamente da MongoDB

    @Field(name = "id")
    private long gameId;  // Questo è il tuo campo personalizzato che desideri mantenere

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
