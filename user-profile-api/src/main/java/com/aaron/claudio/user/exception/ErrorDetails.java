package com.aaron.claudio.user.exception;

import java.time.LocalDateTime;

public record ErrorDetails(
        LocalDateTime timestamp,
        String message,
        String detail,
        String errorCode
) {
}
