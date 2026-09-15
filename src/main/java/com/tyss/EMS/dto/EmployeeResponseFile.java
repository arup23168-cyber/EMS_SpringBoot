package com.tyss.EMS.dto;

import com.tyss.EMS.entity.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeResponseFile {

    private String name;
    private String email;
    private String mobile;
    private Double salary;

    private Address address;

    // Department
    private Integer departmentId;
    private String departmentName;

    // Leave balances
    private Integer sickLeaveBal;
    private Integer casualLeaveBal;
    private Integer unpaidLeaveBal;

    // Files
    private List<EmployeeFileResponse> files;
}