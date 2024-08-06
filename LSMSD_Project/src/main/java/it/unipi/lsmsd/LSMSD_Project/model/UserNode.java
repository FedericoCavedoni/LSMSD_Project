package it.unipi.lsmsd.LSMSD_Project.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Node
public class UserNode {
    @Id
    private String username;

    // Costruttore che accetta un parametro
    public UserNode(String username) {
        this.username = username;
    }

    // Costruttore di default
    public UserNode() {}
}