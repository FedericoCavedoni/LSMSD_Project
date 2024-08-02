package it.unipi.lsmsd.LSMSD_Project.dto;
import lombok.Data;
import java.util.List;


@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private String numeroDiTelefono;
    private String dataDiNascita;
    private List<String> library;
}