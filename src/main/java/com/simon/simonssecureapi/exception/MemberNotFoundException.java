package com.simon.simonssecureapi.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
    public MemberNotFoundException(Long id) {
        super("Member with id " + id + " not found");
    }
}
