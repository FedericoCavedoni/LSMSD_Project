package it.unipi.lsmsd.LSMSD_Project.dao;

import it.unipi.lsmsd.LSMSD_Project.model.User;
import it.unipi.lsmsd.LSMSD_Project.projections.UserProfileProjection;
import it.unipi.lsmsd.LSMSD_Project.projections.UserUsernameProjection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String Username);
    void deleteByUsername(String username);

    @Query(value = "{}", fields = "{ 'username' : 1 }")
    List<UserUsernameProjection> findAllUsernames(PageRequest pageRequest);

    @Query(value = "{ 'username' : ?0 }", fields = "{ 'username' : 1, 'library' : 1 }")
    UserProfileProjection findUserProfileByUsername(String username);
}