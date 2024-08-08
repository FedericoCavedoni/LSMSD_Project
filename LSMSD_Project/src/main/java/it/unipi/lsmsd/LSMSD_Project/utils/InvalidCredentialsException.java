package it.unipi.lsmsd.LSMSD_Project.utils;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}