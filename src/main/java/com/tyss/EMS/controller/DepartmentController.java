package com.tyss.EMS.controller;

import com.tyss.EMS.dto.DepartmentResponseDto;
import com.tyss.EMS.dto.ResponseStructureDto;
import com.tyss.EMS.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DepartmentController {


    private final DepartmentService departmentService;

    //create department
    @PostMapping("/department")
    public ResponseStructureDto saveDepartment(@RequestParam String departmentName){

        DepartmentResponseDto departmentResponseDto = departmentService.saveDepartment(departmentName);

        return ResponseStructureDto.builder()
                .error(false)
                .message("Department is save")
                .data(departmentResponseDto)
                .build();
    }

    //assign department
    @PutMapping("/department/{deptId}/employee/{empId}")
    public ResponseStructureDto assignDepartmentToEmployee(@PathVariable Integer deptId,@PathVariable Integer empId ){
        departmentService.assignDepartment(deptId,empId);

        return ResponseStructureDto.builder()
                .error(false)
                .message("Department assigned to employee successfully")
                .data(null)
                .build();
    }


}
