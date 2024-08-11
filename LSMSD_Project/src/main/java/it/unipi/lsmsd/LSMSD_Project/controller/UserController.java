package it.unipi.lsmsd.LSMSD_Project.controller;

import it.unipi.lsmsd.LSMSD_Project.model.User;
import it.unipi.lsmsd.LSMSD_Project.service.UserService;
import it.unipi.lsmsd.LSMSD_Project.utils.UserAlreadyExistsException;
import it.unipi.lsmsd.LSMSD_Project.utils.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;


import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerNewUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestParam String username,
            @RequestParam String password,HttpSession session) {
        try {
            User user = userService.authenticate(username, password);
            session.setAttribute("user", user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }



    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam int n) {
        List<User> users = userService.getAllUsers(n);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/getByUsername")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            return new ResponseEntity<>(currentUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpSession session) {
        session.invalidate(); // Invalida la sessione corrente
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(@RequestParam(required = false) String username, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }

        if (username == null) {
            // L'utente sta cercando di eliminare il proprio account
            userService.deleteUserByUsername(currentUser.getUsername());
            session.invalidate(); // Invalida la sessione dopo l'eliminazione
            return ResponseEntity.ok("Account eliminato con successo.");
        } else {
            // Un admin sta cercando di eliminare l'account di un altro utente
            if (currentUser.isAdmin()) {
                // Verifica se l'utente esiste prima di procedere con l'eliminazione
                User userToDelete = userService.getUserByUsername(username);
                if (userToDelete == null) {
                    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                }

                userService.deleteUserByUsername(username);
                return ResponseEntity.ok("Account eliminato con successo.");
            } else {
                return new ResponseEntity<>("Operazione non autorizzata", HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam String username) {
        User user = userService.getUserProfile(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUserProfile(HttpSession session, @RequestBody User updatedUser) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            User user = userService.updateUserProfile(updatedUser, currentUser.getUsername());
            if (user != null) {
                session.setAttribute("user", user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/library")
    public ResponseEntity<?> getUserLibrary(@RequestParam String username) {
        List<String> library = userService.getUserLibrary(username);
        if (library != null) {
            return new ResponseEntity<>(library, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found or library is empty", HttpStatus.NOT_FOUND);
        }
    }
}

