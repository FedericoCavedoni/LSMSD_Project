package it.unipi.lsmsd.LSMSD_Project.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("BoardGame")
@Setter
@Getter
public class BoardGameNode {
    @Id
    private long gameId;  // Cambiato da String a Integer
    private String name;

    public BoardGameNode() {}

    public BoardGameNode(long id, String name) {
        this.gameId = id;
        this.name = name;
    }
}
