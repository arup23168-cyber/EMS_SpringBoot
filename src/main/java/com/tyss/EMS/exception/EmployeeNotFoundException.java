package com.tyss.EMS.exception;

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(String msg) {
        super(msg);
    }
}
