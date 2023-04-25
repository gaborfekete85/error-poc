package com.juliusbaer.codility.error.exceptions;

import java.util.UUID;

public class UserNotFoundException extends Exception {
    private final UUID userId;

    public UserNotFoundException(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}