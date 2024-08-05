package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Data
@Document(collection = "Match")
public class Match {
    @Id
    private String id;
    private int numPlayers;
    private boolean result;
    private int duration;

}
