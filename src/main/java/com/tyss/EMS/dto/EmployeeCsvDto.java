package com.tyss.EMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCsvDto {

    private String name;
    private String email;
    private String mobile;
    private Double salary;
    private String password;
    private Integer departmentId;
}