package com.kite.kolesnikov.projectservice.exception;

public class StorageSpaceExceededException extends RuntimeException {
    public StorageSpaceExceededException(String message) {
        super(message);
    }
}
