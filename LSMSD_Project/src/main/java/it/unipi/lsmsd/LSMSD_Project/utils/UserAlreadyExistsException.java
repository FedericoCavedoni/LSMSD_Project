package it.unipi.lsmsd.LSMSD_Project.utils;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}