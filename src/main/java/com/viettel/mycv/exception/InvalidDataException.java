package com.viettel.mycv.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String invalidTokenType) {
        super(invalidTokenType);
    }
}
