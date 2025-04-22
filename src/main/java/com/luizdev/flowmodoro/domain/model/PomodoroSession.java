package com.luizdev.flowmodoro.domain.model;

import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private static final int MIN_DURATION = 15;
    private static final int MAX_DURATION = 120;
    private static final int MIN_BREAK = 5;
    private static final int MAX_BREAK = 30;

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


}
