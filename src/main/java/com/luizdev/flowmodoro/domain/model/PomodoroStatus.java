package com.luizdev.flowmodoro.domain.model;

/**
 * Represents the lifecycle status of a Pomodoro session.
 */

public enum PomodoroStatus {
    /** Session is scheduled to start in the future */
    SCHEDULED,

    /** Session is currently in progress */
    IN_PROGRESS,

    /** Session has been successfully completed */
    FINISHED,

    /** Session was manually cancelled before finishing */
    CANCELED
}
