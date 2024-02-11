package org.example.Exception;

import lombok.Data;

@Data
public class InvalidInputException extends RuntimeException{
    private String msg;

    public InvalidInputException(String msg) {
        this.msg = msg;
    }
}
