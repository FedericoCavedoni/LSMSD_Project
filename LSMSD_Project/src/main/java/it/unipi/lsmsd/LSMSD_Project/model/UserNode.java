package it.unipi.lsmsd.LSMSD_Project.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Node("User")
public class UserNode {
    @Id
    private String username;

    public UserNode(String username) {
        this.username = username;
    }

    public UserNode() {}
}