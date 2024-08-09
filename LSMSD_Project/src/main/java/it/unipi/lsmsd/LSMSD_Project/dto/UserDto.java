package it.unipi.lsmsd.LSMSD_Project.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String username;
    private String nome;
    private String cognome;
    private String email;
    private String numero;
    private String dataNascita;
    private List<String> library;
}
