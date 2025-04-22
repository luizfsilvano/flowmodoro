package com.luizdev.flowmodoro.domain.repository;

import com.luizdev.flowmodoro.domain.model.PomodoroSession;

import java.util.Optional;
import java.util.UUID;

public interface PomodoroSessionRepository {

    PomodoroSession save(PomodoroSession session);
    Optional<PomodoroSession> findById(UUID id);
}
