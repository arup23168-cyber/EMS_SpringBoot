package com.tyss.EMS.service;


import com.tyss.EMS.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
    public EmployeeResponse saveEmployee(EmployeeRequest employee);

    public EmployeeResponse getById(Integer id);

    public List<EmployeeResponse> getAllEmployee();

    public EmployeeResponse updateEmployee(Integer id, EmployeeUpdate employeeDTO);

    public EmployeeResponse partialUpdateEmployee(Integer id, EmployeeUpdate employeeDTO);

    public String deleteEmployee(Integer id);

    Page<EmployeeResponse> getAllEmployeeByPagination(Pageable pageable);

//    Page<EmployeeResponse> getAllEmployeeByPagination(Integer pageNo,Integer pageSize,String field);

    public EmployeeV2Response saveEmployeeV2(EmployeeV2Request employeeRequest);

    EmployeePageResponse getEmployeeV2(String search, Pageable pageable);

    public List<EmployeeResponse> getAllEmployeesOptimization();

    public EmployeeResponseFile saveEmployeeWithPhoto(@Valid EmployeeFileRequest employeeRequest, MultipartFile photo)throws IOException;
}
