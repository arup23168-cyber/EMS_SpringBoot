package com.tyss.EMS.service;

import com.tyss.EMS.dto.DepartmentResponseDto;
import com.tyss.EMS.entity.Department;
import com.tyss.EMS.entity.Employee;
import com.tyss.EMS.exception.DepartmentNotFoundException;
import com.tyss.EMS.exception.EmployeeNotFoundException;
import com.tyss.EMS.repository.DepartmentRepository;
import com.tyss.EMS.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;
    
    @Override
    public DepartmentResponseDto saveDepartment(String departmentName){

        Department department=new Department();
        department.setDeptName(departmentName);

        Department save = departmentRepository.save(department);

        DepartmentResponseDto dto = new DepartmentResponseDto();
        dto.setDepartmentName(save.getDeptName());

        return dto;
    }

    @Override
    public void assignDepartment(Integer deptid,Integer empid){

        Employee employee = employeeRepository
                .findById(empid)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        Department department = departmentRepository
                .findById(deptid)
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        employee.setDepartment(department);

        employeeRepository.save(employee);
    }
}

