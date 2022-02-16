package com.example.rabbit;

public class RetriesExhaustedException extends RuntimeException{
    public RetriesExhaustedException(Throwable cause) {
        super(cause.getCause() != null ? cause.getCause().getMessage() : cause.getMessage());
    }
}
