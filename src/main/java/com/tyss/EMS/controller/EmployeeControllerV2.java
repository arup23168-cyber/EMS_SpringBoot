package com.tyss.EMS.controller;

import com.tyss.EMS.dto.*;
import com.tyss.EMS.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class EmployeeControllerV2 {

    private final EmployeeService employeeService;

    //version 2 of Employee
    @PostMapping("/employee")
    public ResponseStructureDto createEmployeeNew(@Valid @RequestBody EmployeeV2Request employeeV2Request){
        return ResponseStructureDto.builder()
                .error(false)
                .message("Employee is save")
                .data(employeeService.saveEmployeeV2(employeeV2Request))
                .build();
    }

    //get all employee +pagination +sorting

    @GetMapping("/employees/page")
//    public ResponseStructureDto getAllEmployeesByPagination(Pageable pageable) {
//
//        Page<EmployeeResponse> employees = employeeService.getAllEmployeeByPagination(pageable);
//
//        return new ResponseStructureDto(false, "Fetch all employees", employees);
//    }

//    @GetMapping("/employees/page/{pageNo}/{pageSize}/{field}")
//    public ResponseStructureDto getAllEmployeesByPagination(@PathVariable Integer pageNo,@PathVariable Integer pageSize,@PathVariable String field) {
//
//        Page<EmployeeResponse> employees = employeeService.getAllEmployeeByPagination(pageNo,pageSize,field);
//
//        return new ResponseStructureDto(false, "Fetch all employees", employees);
//    }

    public EmployeePageResponse getAllEmployeesByPagination(@RequestParam(required = false) String search,
                                                            @PageableDefault(
                                                                    page = 0,
                                                                    size = 10,
                                                                    direction = Sort.Direction.ASC
                                                            ) Pageable pageable) {

//        Page<EmployeeResponse> employees = employeeService.getAllEmployeeByPagination(pageable);
//        return new ResponseStructureDto(false, "Fetch all employees", employees);

        return employeeService.getEmployeeV2(search,pageable);
    }

}
