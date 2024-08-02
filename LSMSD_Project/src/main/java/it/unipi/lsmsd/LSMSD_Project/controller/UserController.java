package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.dto.UserDTO;
import it.unipi.lsmsd.LSMSD_Project.model.User;
import it.unipi.lsmsd.LSMSD_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import it.unipi.lsmsd.LSMSD_Project.mapper.UserMapper;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User>  registerUser(@RequestBody UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        User newUser = userService.registerNewUser(user);
        return ResponseEntity.ok(newUser);
    }
}