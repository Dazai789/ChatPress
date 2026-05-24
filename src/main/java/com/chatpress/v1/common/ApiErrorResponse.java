package com.chatpress.v1.common;

public record ApiErrorResponse(
        String code,
        String message
) {
}
