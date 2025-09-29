package com.disi.identyService.advice;
import java.beans.Transient;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ErrorEntity(
        LocalDateTime timeStamp,
        String message,
        @Transient
        String errorAuthor,
        int httpStatus
) {
}
