package it.unipi.lsmsd.LSMSD_Project.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class ReviewDTO {
    private int rate;
    private String comment;
    private Timestamp date;
}
