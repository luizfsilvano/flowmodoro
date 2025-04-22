package com.luizdev.flowmodoro.domain.model;

import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain model representing a Pomodoro session for study tracking.
 * Encapsulates rules for creation and state transitions.
 */

@Getter // Public Getters
@RequiredArgsConstructor(access = AccessLevel.PRIVATE) // Private Constructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true) // No Args Private Constructor
public class PomodoroSession {
    private final UUID id;
    private final String theme;
    private final int durationMinutes;
    private final int breakMinutes;
    private final LocalDateTime dateScheduling;
    private final PomodoroStatus status;

    // Erro messages constants
    private static final String INVALID_THEME_MSG = "Theme must not be null or empty";
    private static final String INVALID_DURATION_MSG = "Duration must be between 15 and 120 minutes";
    private static final String INVALID_BREAK_MSG = "Break must be between 5 and 30 minutes";
    private static final String INVALID_SCHEDULE_DATE = "Date must not be in the past";
    private static final String INVALID_SCHEDULE_START_STATUS = "Only Scheduled sessions can be started";
    private static final String INVALID_SCHEDULE_FINISH_STATUS = "Only in progress sessions can be finished";
    private static final String INVALID_SCHEDULE_CANCEL_STATUS = "Only scheduled or in progress sessions can be canceled";
    private static final int MIN_DURATION = 15;
    private static final int MAX_DURATION = 120;
    private static final int MIN_BREAK = 5;
    private static final int MAX_BREAK = 30;

    /**
     * Creates a new Pomodoro session with validation rules.
     * If a scheduling date is provided, the session will be marked as SCHEDULED.
     * Otherwise, the session will start immediately (IN_PROGRESS).
     *
     * @param theme            The study theme (non-null, non-empty)
     * @param durationMinutes  Study time in minutes (15 to 120)
     * @param breakMinutes     Break time in minutes (5 to 30)
     * @param scheduleTime     Optional scheduling date (must not be in the past)
     * @return a new PomodoroSession instance
     * @throws IllegalArgumentException if any parameter is invalid
     */

    public static PomodoroSession schedule(String theme, int durationMinutes, int breakMinutes, LocalDateTime scheduleTime)
    {
        validateParameters(theme, durationMinutes, breakMinutes, scheduleTime);

        return new PomodoroSession(UUID.randomUUID(), theme, durationMinutes, breakMinutes, determineScheduledTime(scheduleTime), determineStatus(scheduleTime));
    }

    public static void validateParameters(String theme, int durationMinutes, int breakMinutes, LocalDateTime dateScheduling)
    {
        if (theme == null || theme.isBlank() )
        {
            throw new IllegalArgumentException(INVALID_THEME_MSG);
        }
        if (durationMinutes < MIN_DURATION || durationMinutes > MAX_DURATION) {
            throw new IllegalArgumentException(INVALID_DURATION_MSG);
        }
        if(breakMinutes < MIN_BREAK || breakMinutes > MAX_BREAK)
        {
            throw new IllegalArgumentException(INVALID_BREAK_MSG);
        }
        if (dateScheduling != null && dateScheduling.isBefore(LocalDateTime.now()))
        {
            throw new IllegalArgumentException(INVALID_SCHEDULE_DATE);
        }
    }

    private static LocalDateTime determineScheduledTime(LocalDateTime proposedTime)
    {
        return proposedTime != null ? proposedTime : LocalDateTime.now();
    }

    private static PomodoroStatus determineStatus(LocalDateTime scheduledTime)
    {
        return scheduledTime != null ? PomodoroStatus.SCHEDULED : PomodoroStatus.IN_PROGRESS;
    }

    // Start session (change status)
    public PomodoroSession start()
    {
        if (this.status != PomodoroStatus.SCHEDULED) throw new IllegalArgumentException(INVALID_SCHEDULE_START_STATUS);
        return new PomodoroSession(this.id, this.theme, this.durationMinutes, this.breakMinutes, this.dateScheduling, PomodoroStatus.IN_PROGRESS);
    }

    // Finish Session
    public PomodoroSession finish()
    {
        if (this.status != PomodoroStatus.IN_PROGRESS) throw new IllegalArgumentException(INVALID_SCHEDULE_FINISH_STATUS);
        return new PomodoroSession(this.id, this.theme, this.durationMinutes, this.breakMinutes, this.dateScheduling, PomodoroStatus.FINISHED);
    }

    // Cancel Session
    public PomodoroSession cancel()
    {
        if (this.status != PomodoroStatus.IN_PROGRESS && this.status != PomodoroStatus.SCHEDULED) throw new IllegalArgumentException(INVALID_SCHEDULE_CANCEL_STATUS);
        return new PomodoroSession(this.id, this.theme, this.durationMinutes, this.breakMinutes, this.dateScheduling, PomodoroStatus.CANCELED);
    }

}
