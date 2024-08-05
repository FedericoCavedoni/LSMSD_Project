package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Data
@Document(collection = "BoardGame")
public class BoardGame {
    @Id
    private String id;
    private String name;
    private String category;
    private float rating;
    private String description;

}
