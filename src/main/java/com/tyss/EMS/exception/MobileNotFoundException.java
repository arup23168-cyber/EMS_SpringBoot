package com.tyss.EMS.exception;

public class MobileNotFoundException extends RuntimeException {
    public MobileNotFoundException(String msg){
        super(msg);
    }
}
