package com.luizdev.flowmodoro;

import com.luizdev.flowmodoro.domain.model.PomodoroSession;
import com.luizdev.flowmodoro.domain.model.PomodoroStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PomodoroSessionTest {
    @Test
    void shouldCreateScheduledSessionSuccessfully()
    {
        String theme = "Spring Boot Study";
        int durationMinutes = 120;
        int breakMinutes = 30;
        LocalDateTime dateScheduling = LocalDateTime.now().plusMinutes(20);

        PomodoroSession session = PomodoroSession.schedule(theme, durationMinutes, breakMinutes, dateScheduling);

        assertEquals(theme, session.getTheme());
        assertEquals(durationMinutes, session.getDurationMinutes());
        assertEquals(breakMinutes, session.getBreakMinutes());
        assertEquals(dateScheduling, session.getDateScheduling());
        assertEquals(PomodoroStatus.SCHEDULED, session.getStatus());

    }

    @Test
    void shouldThrowExceptionWhenThemeIsBlank()
    {
        String theme = "";
        int durationMinutes = 120;
        int breakMinutes = 30;
        LocalDateTime dateScheduling = LocalDateTime.now().plusMinutes(20);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PomodoroSession.schedule(theme, durationMinutes, breakMinutes, dateScheduling);
        });

        assertEquals("Theme must not be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenThemeIsNull()
    {
        String theme = null;
        int durationMinutes = 120;
        int breakMinutes = 30;
        LocalDateTime dateScheduling = LocalDateTime.now().plusMinutes(20);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PomodoroSession.schedule(theme, durationMinutes, breakMinutes, dateScheduling);
        });

        assertEquals("Theme must not be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDurationMinutesIsOutOfRange()
    {
        String theme = "Spring Boot Study";
        int durationMinutes = 5;
        int breakMinutes = 30;
        LocalDateTime dateScheduling = LocalDateTime.now().plusMinutes(20);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PomodoroSession.schedule(theme, durationMinutes, breakMinutes, dateScheduling);
        });

        assertEquals("Duration must be between 15 and 120 minutes", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBreakMinutesIsOutOfRange()
    {
        String theme = "Spring Boot Study";
        int durationMinutes = 120;
        int breakMinutes = 60;
        LocalDateTime dateScheduling = LocalDateTime.now().plusMinutes(20);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PomodoroSession.schedule(theme, durationMinutes, breakMinutes, dateScheduling);
        });

        assertEquals("Break must be between 5 and 30 minutes", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDateSchedulingIsInPast()
    {
        String theme = "Spring Boot Study";
        int durationMinutes = 120;
        int breakMinutes = 20;
        LocalDateTime dateScheduling = LocalDateTime.now().minusDays(10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PomodoroSession.schedule(theme, durationMinutes, breakMinutes, dateScheduling);
        });

        assertEquals("Date must not be in the past", exception.getMessage());
    }

    @Test
    void shouldStartScheduledSessionSuccessfully() {
        PomodoroSession scheduledSession = PomodoroSession.schedule(
                "Java Deep Dive",
                50,
                10,
                LocalDateTime.now().plusMinutes(30)
        );

        PomodoroSession startedSession = scheduledSession.start();

        assertEquals(PomodoroStatus.IN_PROGRESS, startedSession.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenStartingNonScheduledSession() {
        PomodoroSession inProgressSession = PomodoroSession.schedule(
                "Kotlin Playground",
                25,
                5,
                null // sem data = IN_PROGRESS
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, inProgressSession::start);
        assertEquals("Only Scheduled sessions can be started", exception.getMessage());
    }

    @Test
    void shouldFinishInProgressSessionSuccessfully() {
        PomodoroSession inProgressSession = PomodoroSession.schedule(
                "Spring Docs",
                40,
                10,
                null // IN_PROGRESS
        );

        PomodoroSession finishedSession = inProgressSession.finish();

        assertEquals(PomodoroStatus.FINISHED, finishedSession.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenFinishingNonInProgressSession() {
        PomodoroSession scheduledSession = PomodoroSession.schedule(
                "Error handling",
                30,
                5,
                LocalDateTime.now().plusMinutes(15) // SCHEDULED
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, scheduledSession::finish);
        assertEquals("Only in progress sessions can be finished", exception.getMessage());
    }

    @Test
    void shouldCancelScheduledSessionSuccessfully() {
        PomodoroSession scheduledSession = PomodoroSession.schedule(
                "TDD Flow",
                25,
                5,
                LocalDateTime.now().plusMinutes(15)
        );

        PomodoroSession canceledSession = scheduledSession.cancel();

        assertEquals(PomodoroStatus.CANCELED, canceledSession.getStatus());
    }

    @Test
    void shouldCancelInProgressSessionSuccessfully() {
        PomodoroSession inProgressSession = PomodoroSession.schedule(
                "JUnit Advanced",
                45,
                15,
                null
        );

        PomodoroSession canceledSession = inProgressSession.cancel();

        assertEquals(PomodoroStatus.CANCELED, canceledSession.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCancelingFinishedSession() {
        PomodoroSession inProgressSession = PomodoroSession.schedule(
                "Refactor and Clean Code",
                40,
                10,
                null
        );

        PomodoroSession finishedSession = inProgressSession.finish();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, finishedSession::cancel);
        assertEquals("Only scheduled or in progress sessions can be canceled", exception.getMessage());
    }

}
