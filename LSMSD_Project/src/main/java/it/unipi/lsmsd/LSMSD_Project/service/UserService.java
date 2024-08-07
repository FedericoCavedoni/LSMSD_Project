package it.unipi.lsmsd.LSMSD_Project.service;

import it.unipi.lsmsd.LSMSD_Project.dao.UserRepository;
import it.unipi.lsmsd.LSMSD_Project.dao.UserNodeRepository;
import it.unipi.lsmsd.LSMSD_Project.model.User;
import it.unipi.lsmsd.LSMSD_Project.model.UserNode;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import it.unipi.lsmsd.LSMSD_Project.utils.UserAlreadyExistsException;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserNodeRepository userNodeRepository;

    //@Autowired
    //private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User registerNewUser(User user) {
        // Verifica se l'username esiste già
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username già esistente: " + user.getUsername());
        }
        // Salva l'utente in MongoDB
        User savedUser = userRepository.save(user);

        // Salva solo l'username in Neo4j
        UserNode userNode = new UserNode(user.getUsername());
        userNodeRepository.save(userNode);

        return savedUser;
    }
}