package com.tyss.EMS.exception;

import com.tyss.EMS.dto.ResponseStructureDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseStructureDto handleEmailException(EmailNotFoundException exception){
        return ResponseStructureDto.builder().error(true).message(exception.getMessage()).build();
    }

    @ExceptionHandler(MobileNotFoundException.class)
    public ResponseStructureDto handleMobilelException(MobileNotFoundException exception){
        return ResponseStructureDto.builder().error(true).message(exception.getMessage()).build();
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseStructureDto handleIdNotFoundException(IdNotFoundException exception){
        return ResponseStructureDto.builder().error(true).message(exception.getMessage()).build();
    }
    @ExceptionHandler(NoRecordFoundException.class)
    public ResponseStructureDto handleNoRecordFound(NoRecordFoundException exception){
        return ResponseStructureDto.builder().error(true).message(exception.getMessage()).build();
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseStructureDto handleNoRecordFound(EmployeeNotFoundException exception){
        return ResponseStructureDto.builder().error(true).message(exception.getMessage()).build();
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseStructureDto handleNoRecordFound(DepartmentNotFoundException exception){
        return ResponseStructureDto.builder().error(true).message(exception.getMessage()).build();
    }

    @ExceptionHandler(GenericException.class)
    public ResponseStructureDto handleNoRecordFound(GenericException exception){
        return ResponseStructureDto.builder().error(true).message(exception.getMessage()).build();
    }
}
