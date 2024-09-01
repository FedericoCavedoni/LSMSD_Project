package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.UserRepository;
import it.unipi.lsmsd.LSMSD_Project.dao.UserNodeRepository;
import it.unipi.lsmsd.LSMSD_Project.model.Game;
import it.unipi.lsmsd.LSMSD_Project.model.Relation;
import it.unipi.lsmsd.LSMSD_Project.model.User;
import it.unipi.lsmsd.LSMSD_Project.model.UserNode;
import it.unipi.lsmsd.LSMSD_Project.projections.UserOnlyUsernameProjection;
import it.unipi.lsmsd.LSMSD_Project.projections.UserProfileProjection;
import it.unipi.lsmsd.LSMSD_Project.projections.UserUsernameProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import it.unipi.lsmsd.LSMSD_Project.utils.UserAlreadyExistsException;
import it.unipi.lsmsd.LSMSD_Project.utils.InvalidCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

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


    public List<UserUsernameProjection> getAllUsernames(int n) {
        return userRepository.findAllUsernames(PageRequest.of(0, n));
    }

    public List<UserUsernameProjection> findUsernamesByPartialName(String partialName) {
        return userRepository.findUsernamesContainingIgnoreCase(partialName);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserOnlyUsernameProjection getOnlyUsernameByUsername(String username) {
        return userRepository.findOnlyUsernameByUsername(username);
    }

    public List<Relation> getUserRelationships(String username, String relation, int num) {
        return userNodeRepository.findUserRelationships(username, relation, num);
    }

    public UserProfileProjection getUserProfile(String username) {
        return userRepository.findUserProfileByUsername(username);
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username);
        userNodeRepository.deleteByUsername(username);
    }

    public User updateUserProfile(User updatedUser, String username) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {

            existingUser.setNome(updatedUser.getNome());
            existingUser.setCognome(updatedUser.getCognome());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setNumero(updatedUser.getNumero());
            existingUser.setDataNascita(updatedUser.getDataNascita());
            existingUser.setLibrary(updatedUser.getLibrary());

            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    public List<Game> getUserLibrary(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getLibrary();
        }
        return null;
    }

}
