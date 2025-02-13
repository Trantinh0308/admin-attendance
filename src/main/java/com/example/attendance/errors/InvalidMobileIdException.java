package com.example.attendance.errors;

public class InvalidMobileIdException extends Exception{
    public InvalidMobileIdException(String message) {
        super(message);
    }

    public InvalidMobileIdException() {
        super("Invalid Mobile ID");
    }
}
