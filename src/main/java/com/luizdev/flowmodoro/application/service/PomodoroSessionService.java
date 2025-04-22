package com.luizdev.flowmodoro.application.service;

import com.luizdev.flowmodoro.domain.exception.PomodoroSessionNotFoundException;
import com.luizdev.flowmodoro.domain.model.PomodoroSession;
import com.luizdev.flowmodoro.domain.repository.PomodoroSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PomodoroSessionService {

    private final PomodoroSessionRepository repository;

    private void updateSession(UUID id, Function<PomodoroSession, PomodoroSession> transition) {
        PomodoroSession session = getSession(id);
        repository.save(transition.apply(session));
    }


    public UUID createSession(String theme, int duration, int breakTime, LocalDateTime schedulingDate)
    {
        PomodoroSession session = PomodoroSession.schedule(theme, duration, breakTime, schedulingDate);
        return repository.save(session).getId();
    }

    public void startSession(UUID id) {
        updateSession(id, PomodoroSession::start);
    }

    public void finishSession(UUID id) {
        updateSession(id, PomodoroSession::finish);
    }

    public void cancelSession(UUID id) {
        updateSession(id, PomodoroSession::cancel);
    }

    private PomodoroSession getSession(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new PomodoroSessionNotFoundException(id));
    }
}
