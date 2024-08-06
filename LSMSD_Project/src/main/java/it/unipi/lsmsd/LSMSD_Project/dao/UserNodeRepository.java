package it.unipi.lsmsd.LSMSD_Project.dao;
import it.unipi.lsmsd.LSMSD_Project.model.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode, String> {
}