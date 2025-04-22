package com.luizdev.flowmodoro.domain.exception;

import java.util.UUID;

public class PomodoroSessionNotFoundException extends RuntimeException {
    public PomodoroSessionNotFoundException(UUID id) {
        super("Session not found with ID: " + id);
    }
}