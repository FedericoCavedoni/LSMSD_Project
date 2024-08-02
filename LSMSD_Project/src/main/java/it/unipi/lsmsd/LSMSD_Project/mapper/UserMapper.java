package it.unipi.lsmsd.LSMSD_Project.mapper;

import it.unipi.lsmsd.LSMSD_Project.dto.UserDTO;
import it.unipi.lsmsd.LSMSD_Project.model.User;

public class UserMapper {

    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setNome(userDTO.getNome());
        user.setCognome(userDTO.getCognome());
        user.setNumeroDiTelefono(userDTO.getNumeroDiTelefono());
        user.setDataDiNascita(userDTO.getDataDiNascita());
        user.setLibrary(userDTO.getLibrary());
        return user;
    }

    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setNome(user.getNome());
        userDTO.setCognome(user.getCognome());
        userDTO.setNumeroDiTelefono(user.getNumeroDiTelefono());
        userDTO.setDataDiNascita(user.getDataDiNascita());
        userDTO.setLibrary(user.getLibrary());
        return userDTO;
    }
}
