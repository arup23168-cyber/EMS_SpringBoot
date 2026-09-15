package com.tyss.EMS.service;

import com.tyss.EMS.dto.DepartmentResponseDto;


public interface DepartmentService {
    public DepartmentResponseDto saveDepartment(String departmentName) ;

    void assignDepartment(Integer deptid, Integer empid);
}
