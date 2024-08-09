package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.UserRepository;
import it.unipi.lsmsd.LSMSD_Project.dao.UserNodeRepository;
import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import it.unipi.lsmsd.LSMSD_Project.model.User;
import it.unipi.lsmsd.LSMSD_Project.model.UserNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import it.unipi.lsmsd.LSMSD_Project.utils.UserAlreadyExistsException;
import it.unipi.lsmsd.LSMSD_Project.utils.InvalidCredentialsException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserNodeRepository userNodeRepository;

    public User registerNewUser(User user) {

        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username già esistente: " + user.getUsername());
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        User savedUser = userRepository.save(user);

        UserNode userNode = new UserNode(user.getUsername());
        userNodeRepository.save(userNode);

        return savedUser;
    }
    public User authenticate(String username, String password) throws InvalidCredentialsException {
        User user = userRepository.findByUsername(username);
        if (user != null && password != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }


    public List<User> getAllUsers(int n) {
        return userRepository.findAll(PageRequest.of(0, n)).getContent();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<Relation> getUserRelationships(String username, String relation, int num) {
        return userNodeRepository.findUserRelationships(username, relation, num);
    }

    public User getUserProfile(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            User limitedUser = new User();
            limitedUser.setUsername(user.getUsername());
            limitedUser.setLibrary(user.getLibrary());
            return limitedUser;
        } else {
            return null;
        }
    }
}
