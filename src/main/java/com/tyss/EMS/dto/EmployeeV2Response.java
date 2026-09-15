package com.tyss.EMS.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmployeeV2Response {

    private String name;

    private String email;

    private String mobileNo;

    private AddressResponse addressDto;

    private DepartmentResponseDto departmentResponseDto;

    private Integer sickLeaveBal;

    private Integer casualLeaveBal;

    private Integer unpaidLeaveBal;

}
