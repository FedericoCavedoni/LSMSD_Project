package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;


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

    //get set
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        this.Cognome = cognome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getNumeroDiTelefono() {
        return Numero;
    }

    public void setNumeroDiTelefono(String numeroDiTelefono) {
        this.Numero = numeroDiTelefono;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getDataDiNascita() {
        return DataNascita;
    }

    public void setDataDiNascita(String dataDiNascita) {
        this.DataNascita = dataDiNascita;
    }

    public List<String> getLibrary() {
        return library;
    }

    public void setLibrary(List<String> library) {
        this.library = library;
    }

}
