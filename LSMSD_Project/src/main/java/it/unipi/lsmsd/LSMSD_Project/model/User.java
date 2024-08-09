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
@Document(collection = "User")
public class User {
    @Id
    private String id;

    @Field(name = "username")
    private String username;

    @Field(name = "nome")
    private String nome;

    @Field(name = "cognome")
    private String cognome;

    @Field(name = "email")
    private String email;

    @Field(name = "numero di telefono")
    private String numero;

    @Field(name = "password")
    private String password;

    @Field(name = "data di nascita")
    private String dataNascita;

    @Field(name = "library")
    private List<String> library;
}
