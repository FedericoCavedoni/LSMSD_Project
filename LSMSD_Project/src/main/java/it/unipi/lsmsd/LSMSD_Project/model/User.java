package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Setter
@Getter
@Data
@Document(collection = "User")
public class User {
    @Id
    private String id;
    private String Username;
    private String Nome;
    private String Cognome;
    private String Email;
    private String Numero;
    private String Password;
    private String DataNascita;
    private List<String> library;
}
