package com.tyss.EMS.exception;

public class NoRecordFoundException extends RuntimeException{
    public NoRecordFoundException(String msg){
        super(msg);
    }
}
