package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.sql.Timestamp;

@Setter
@Getter
@Data
@Document(collection = "Review")
public class Review {
    @Id
    private String id;
    private int rate;
    private String comment;
    private Timestamp date;

}
