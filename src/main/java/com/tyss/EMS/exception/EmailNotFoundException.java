package com.tyss.EMS.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String msg){
        super(msg);
    }
}
