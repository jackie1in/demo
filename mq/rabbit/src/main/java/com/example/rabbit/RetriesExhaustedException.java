package com.example.rabbit;

public class RetriesExhaustedException extends RuntimeException{
    public RetriesExhaustedException(Throwable cause) {
        super(cause);
    }
}
