package com.marcin_k.mystocks.model.exceptions;

public class NotEnoughRecordsException extends Exception{
    public NotEnoughRecordsException(String message) {
        super(message);
    }
}
