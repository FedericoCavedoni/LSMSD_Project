package it.unipi.lsmsd.LSMSD_Project.projections;

import it.unipi.lsmsd.LSMSD_Project.model.Game;

import java.util.List;

public interface UserProfileProjection {
    String getUsername();
    List<Game> getLibrary();
}

