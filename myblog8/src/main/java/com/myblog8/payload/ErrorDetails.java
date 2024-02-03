package com.myblog8.payload;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorDetails {
    private final Date timestamp;
    private final String message;
    private final String details;

    //Constructor
    public ErrorDetails(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
