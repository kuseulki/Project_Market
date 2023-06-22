package com.example.project.handler;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message){
        super(message);
    }
}
