package com.tyss.EMS.controller;


import com.tyss.EMS.dto.*;
import com.tyss.EMS.entity.Employee;
import com.tyss.EMS.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeControllerV1 {

    private final EmployeeService employeeService;

    private static final Logger log = LoggerFactory.getLogger(EmployeeControllerV1.class);


    //create
    @PostMapping("/employee")
    public ResponseStructureDto createEmployee(@Valid @RequestBody EmployeeRequest employee) {

        log.debug("Employee registration request: {}", employee);
        log.info("Received request to create employee with email: {}", employee.getEmail());
        //using normal way

//        return employeeService.saveEmployee(employee);
//        ResponseStructureDto structureDto=new ResponseStructureDto();
//        structureDto.setError(false);
//        structureDto.setMessage("Employee data is save");
//        structureDto.setData(employeeService.saveEmployee(employee));
//        return structureDto;

        log.info("Employee created successfully with email: {}", employee.getEmail());

        //using the constructor
        return new ResponseStructureDto(false, "Employee data is save", employeeService.saveEmployee(employee));

        //using builder
//        return ResponseStructureDto.builder().error(false).message("Employee data is save").data(employeeService.saveEmployee(employee)).build();

    }


    //getByID

//    @GetMapping("/employee/{id}")
//    public ResponseStructureDto getById(@PathVariable Integer id) {
//
////        return employeeService.getById(id);
//        return new ResponseStructureDto(false, "Employee data is retrieved based in id:" + "id", employeeService.getById(id));
//    }


    /**
     *
     * @RequestBody → validate the DTO fields using @Valid
     * @PathVariable / @RequestParam → validate the method parameter using @Validated + constraint annotations such as @Min, @Max, @Positive, @NotBlank, @Pattern, etc.
     * */
//here i am using @Positive annotation to validate the id parameter, which ensures that the id is a positive integer. If the id is not positive, a validation error will be thrown before the method is executed.
@GetMapping("/employee/{id}")
public ResponseStructureDto getById(
        @PathVariable @Positive Integer id) {

    log.info("Received request in controller to fetch employee with id: {}", id);

//    log.info("Employee fetched successfully with id: {}", id);
    return new ResponseStructureDto(
            false,
            "Employee data is retrieved based on id: " + id,
            employeeService.getById(id)
    );
}

    //getAllEmployee
    @GetMapping("/employees")
    public ResponseStructureDto getAllEmployees() {
        return new ResponseStructureDto(false, "Fetch all the Employee", employeeService.getAllEmployee());
    }

    //getAll Employee is Optimized to sove the N+1 problem
    @GetMapping("/employees/optimized")
    public ResponseStructureDto getAllEmployeesOptimization() {
        return new ResponseStructureDto(false, "Fetch all the Employee", employeeService.getAllEmployeesOptimization());
    }

    //update
    @PutMapping("/employee/{id}")
    public ResponseStructureDto updateEmployee(@PathVariable Integer id, @Valid @RequestBody EmployeeUpdate dto) {
        return new ResponseStructureDto(false, "Employee data is save", employeeService.updateEmployee(id, dto));
    }

    //partial update
    @PatchMapping("/employee/{id}")
    public EmployeeResponse partialUpdateEmployee(@PathVariable Integer id, @RequestBody EmployeeUpdate dto) {
        return employeeService.partialUpdateEmployee(id, dto);
    }

    //delete
    @DeleteMapping("/employee/{id}")
    public String deleteEmployee(@PathVariable Integer id) {
        return employeeService.deleteEmployee(id);
    }


    //here i am create a new API to store the employee as well as files also
    @PostMapping(
            value = "/employees/with-photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseStructureDto createEmployeeWithPhoto(
            @Valid @RequestPart("employee")
            EmployeeFileRequest employeeRequest,

            @RequestPart("photo")
            MultipartFile photo
    ) throws IOException {

        return new ResponseStructureDto(
                false,
                "Employee and photo saved successfully",
                employeeService.saveEmployeeWithPhoto(
                        employeeRequest,
                        photo
                )
        );
    }

}
