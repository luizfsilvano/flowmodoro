package com.luizdev.flowmodoro.application.service;

import com.luizdev.flowmodoro.domain.exception.PomodoroSessionNotFoundException;
import com.luizdev.flowmodoro.domain.model.PomodoroSession;
import com.luizdev.flowmodoro.domain.repository.PomodoroSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PomodoroSessionServiceTest {

    @Mock
    private PomodoroSessionRepository repository;

    @InjectMocks
    private PomodoroSessionService service;

    private final UUID sessionId = UUID.randomUUID();
    private PomodoroSession scheduledSession;

    @BeforeEach
    void setup() {
        scheduledSession = PomodoroSession.schedule("Estudo de java", 60, 5, LocalDateTime.now().plusMinutes(10));
    }

    @Test
    void shouldCreateSessionSuccessfully() {
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UUID result = service.createSession("Estudo Java", 25, 5, scheduledSession.getDateScheduling());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void shouldStartSessionWhenScheduled() {
        when(repository.findById(sessionId)).thenReturn(Optional.of(scheduledSession));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> service.startSession(sessionId));
        verify(repository).save(argThat(session -> session.getStatus().name().equals("IN_PROGRESS")));
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFound() {
        when(repository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(PomodoroSessionNotFoundException.class, () -> service.startSession(sessionId));
    }

    @Test
    void shouldFinishSessionWhenInProgress() {
        PomodoroSession inProgress = scheduledSession.start();
        when(repository.findById(sessionId)).thenReturn(Optional.of(inProgress));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> service.finishSession(sessionId));
        verify(repository).save(argThat(session -> session.getStatus().name().equals("FINISHED")));
    }

    @Test
    void shouldCancelSessionWhenScheduled() {
        when(repository.findById(sessionId)).thenReturn(Optional.of(scheduledSession));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> service.cancelSession(sessionId));
        verify(repository).save(argThat(session -> session.getStatus().name().equals("CANCELED")));
    }

    @Test
    void shouldThrowExceptionWhenFinishingNotStartedSession() {
        when(repository.findById(sessionId)).thenReturn(Optional.of(scheduledSession));

        assertThrows(IllegalArgumentException.class, () -> service.finishSession(sessionId));
    }


}
