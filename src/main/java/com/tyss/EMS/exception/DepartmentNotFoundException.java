package com.tyss.EMS.exception;

public class DepartmentNotFoundException extends RuntimeException{
    public DepartmentNotFoundException(String msg) {
        super(msg);
    }
}
