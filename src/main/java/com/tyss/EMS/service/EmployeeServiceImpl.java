package com.tyss.EMS.service;


import com.tyss.EMS.dto.*;
import com.tyss.EMS.entity.Address;
import com.tyss.EMS.entity.Department;
import com.tyss.EMS.entity.Employee;
import com.tyss.EMS.entity.EmployeeFile;
import com.tyss.EMS.exception.*;
import com.tyss.EMS.repository.DepartmentRepository;
import com.tyss.EMS.repository.EmployeeFileRepository;
import com.tyss.EMS.repository.EmployeeRepository;
import com.tyss.EMS.util.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    //    @Autowired
    private final EmployeeRepository repository;


    private final DepartmentRepository departmentRepository;

    private final EmployeeFileRepository employeeFileRepository;

    //if i am using @Slf4j annotation then
    // i don't need to create a logger object,
    // because @Slf4j annotation automatically creates a logger object for me,
    // so i can use log.debug(), log.info(), log.error() etc. methods to log the messages.

//    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    //create
    @Transactional
    @CacheEvict(
            value = "employeeList",
            allEntries = true
    )
    public EmployeeResponse saveEmployee(EmployeeRequest employee) {

        Optional<Employee> existingEmp = repository.findByEmailOrMobile(employee.getEmail(), employee.getMobile());

        if (existingEmp.isPresent()) {
            Employee optEmp = existingEmp.get();
            boolean isEmailDuplicate = optEmp.getEmail().equalsIgnoreCase(employee.getEmail());
            boolean isMobileDuplicate = optEmp.getMobile().equals(employee.getMobile());

            if (isEmailDuplicate && isMobileDuplicate) {
                throw new EmailNotFoundException("Both email and mobile number already exists try using different details");
            } else if (isEmailDuplicate) {
                throw new EmailNotFoundException("Email already exists try using another email");
            } else {
                throw new MobileNotFoundException("Mobile number already exists try using another number");
            }
        }
        Employee emp = EmployeeMapper.dtoToEntity(employee);
        Employee save = repository.save(emp);
        EmployeeResponse employeeDTO = EmployeeMapper.entityToDto(save);
        return employeeDTO;
    }


    //getBYID
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "employees", key = "#id")
    public EmployeeResponse getById(Integer id) {

        log.info("receive in the service to Fetch employee from database. id: {}", id);

        Employee employee = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Employee not found in the database"));
        EmployeeResponse employeeDTO = EmployeeMapper.entityToDto(employee);

        log.debug("Employee found successfully. id: {}", employee.getId());

        return employeeDTO;
    }


    //get All Employee
    @Transactional(readOnly = true)
    @Cacheable(value = "employeeList")
    public List<EmployeeResponse> getAllEmployee() {
        List<Employee> employeeList = repository.findAll();
        if (employeeList.isEmpty()) {
            throw new NoRecordFoundException("No record found in the database");
        } else {
            List<EmployeeResponse> dtoList = EmployeeMapper.entityToListDto(employeeList);
            return dtoList;
        }

    }

    //get All Employee By Optimized
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployeesOptimization() {

        //Here JOIN FETCH is used to fetch the associated address entities along with the employee entities in a single query, which helps to avoid the N+1 problem.
        List<Employee> employeeList = repository.findAllWithAddress();

        //Using @EntityGraph to fetch the associated address entities along with the employee entities in a single query, which helps to avoid the N+1 problem.
//            List<Employee> employeeList = repository.findAllEmployees();
        if (employeeList.isEmpty()) {
            throw new NoRecordFoundException("No record found in the database");
        } else {
            List<EmployeeResponse> dtoList = EmployeeMapper.entityToListDto(employeeList);
            return dtoList;
        }

    }

    //update
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "employees", key = "#id"),
            @CacheEvict(value = "employeeList", allEntries = true)
    })
    public EmployeeResponse updateEmployee(Integer id, EmployeeUpdate dto) {

        Optional<Employee> optEmployee = repository.findById(id);

        if (optEmployee.isEmpty()) {
            throw new IdNotFoundException("Employee not found with the provided employee id");
        }

        Employee employee = optEmployee.get();
        EmployeeMapper.updateEntityFromDto(dto, employee);
        Employee updatedEmployee = repository.save(employee);
        EmployeeResponse employeeDTO = EmployeeMapper.entityToDto(updatedEmployee);
        return employeeDTO;
    }


    //partial update
//    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "employees", key = "#id"),
            @CacheEvict(value = "employeeList", allEntries = true)
    })
    public EmployeeResponse partialUpdateEmployee(Integer id, EmployeeUpdate dto) {

        Optional<Employee> optionalEmployee = repository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new IdNotFoundException("Employee not found in the database");
        }

        Employee employee = optionalEmployee.get();

        EmployeeMapper.updateEntityFromDto(dto, employee);
        Employee saveEmp = repository.save(employee);
        EmployeeResponse employeeDTO = EmployeeMapper.entityToDto(employee);
        return employeeDTO;
    }

    //delete
    @Transactional
//    @CacheEvict(value = "employees", key = "#id")
    @Caching(evict = {
            @CacheEvict(value = "employees", key = "#id"),
            @CacheEvict(value = "employeeList", allEntries = true)
    })
    public String deleteEmployee(Integer id) {

        Optional<Employee> optionalEmployee = repository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new IdNotFoundException("Employee not found in the database");
        }

        repository.deleteById(id);

        return "Employee deleted successfully";
    }

    //create a version 2
    public EmployeeV2Response saveEmployeeV2(EmployeeV2Request employee) {

        Optional<Employee> existingEmp = repository.findByEmailOrMobile(employee.getEmail(), employee.getMobile());

        if (existingEmp.isPresent()) {
            Employee optEmp = existingEmp.get();
            boolean isEmailDuplicate = optEmp.getEmail().equalsIgnoreCase(employee.getEmail());
            boolean isMobileDuplicate = optEmp.getMobile().equals(employee.getMobile());

            if (isEmailDuplicate && isMobileDuplicate) {
                throw new EmailNotFoundException("Both email and mobile number already exists try using different details");
            } else if (isEmailDuplicate) {
                throw new EmailNotFoundException("Email already exists try using another email");
            } else {
                throw new MobileNotFoundException("Mobile number already exists try using another number");
            }
        }

        //checking the department exist or not
        Department department = departmentRepository
                .findById(employee.getDeptId())
                .orElseThrow(() ->
                        new IdNotFoundException("Department not found"));

        Employee emp = EmployeeMapper.dtoToEntityV2(employee);

        //set the department
        emp.setDepartment(department);

        Employee save = repository.save(emp);

        return EmployeeMapper.entityToDtoV2(save);

    }


    // PAGINATION + SORTING

    @Override
    public Page<EmployeeResponse> getAllEmployeeByPagination(Pageable pageable) {

        Page<Employee> employeePage = repository.findAll(pageable);

        return employeePage.map(EmployeeMapper::entityToDto);
    }


//    @Override
//    public Page<EmployeeResponse> getAllEmployeeByPagination(Integer pageNo,Integer pageSize,String field) {
//
//        Page<Employee> employeePage = repository.findAll(PageRequest.of(pageNo,pageSize, Sort.by(field)));
//
//        return employeePage.map(EmployeeMapper::entityToDto);
//    }

//    @Override
//    public EmployeePageResponse getEmployeeV2(String search, Pageable pageable) {
//
//        Page<Employee> employeePage;
//
//        if (search ==null || search.isBlank()){
//            employeePage=repository.findAll(pageable);
//        }else {
//            employeePage=repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search,search,pageable);
//        }
//
//        EmployeePageResponse pageResponse=new EmployeePageResponse();
//
//        List<EmployeeResponse> employeesResponseList=new ArrayList<>();
//
//        List<Employee> employeeEntityList = employeePage.getContent();//get the employee entity list from page object
//
//        //convert employee entity to dto one by one
//        for (Employee employee:employeeEntityList){
//
//            EmployeeResponse employeeResponse=new EmployeeResponse();
//
//            employeeResponse.setName(employee.getName());
//            employeeResponse.setEmail(employee.getEmail());
//            employeeResponse.setSalary(employee.getSalary());
//            employeeResponse.setMobile(employee.getMobile());
//            employeeResponse.setAddress(employee.getAddress());
//
//            employeeEntityList.add(employeeResponse);
//        }
//        pageResponse.setContent(employeesResponseList);
//        pageResponse.setPage(employeePage.getNumber());
//        pageResponse.setSize(employeePage.getSize());
//        pageResponse.setTotalElements(employeePage.getTotalElements());
//        pageResponse.setTotalPages(employeePage.getTotalPages());
//        return pageResponse;
//    }

    @Override
    public EmployeePageResponse getEmployeeV2(String search, Pageable pageable) {
        Page<Employee> employeePage;

        if (search == null || search.isBlank()) {
            employeePage = repository.findAll(pageable);
        } else {
            employeePage =
                    repository
                            .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                                    search,
                                    search,
                                    pageable);
        }

        EmployeePageResponse pageResponse = new EmployeePageResponse();

        List<EmployeeResponse> employeesResponseList = new ArrayList<>();

        List<Employee> employeeEntityList = employeePage.getContent();//get the employee entity list from page object

        //convert employee entity to dto one by one
        for (Employee employee : employeeEntityList) {

            EmployeeResponse employeeResponse = new EmployeeResponse();//dto


            employeeResponse.setName(employee.getName());
            employeeResponse.setEmail(employee.getEmail());
            employeeResponse.setMobile(employee.getMobile());
            employeeResponse.setSalary(employeeResponse.getSalary());
            employeeResponse.setAddress(employeeResponse.getAddress());

            employeesResponseList.add(employeeResponse);
        }

        pageResponse.setContent(employeesResponseList);
        pageResponse.setPage(employeePage.getNumber());
        pageResponse.setSize(employeePage.getSize());
        pageResponse.setTotalElements(employeePage.getTotalElements());
        pageResponse.setTotalPages(employeePage.getTotalPages());
        return pageResponse;
    }

    //here i create a new API to store the employee data with Photo and CV file, so i create a new method in service layer to handle the request and save the data in database

    @Override
    @Transactional(
            rollbackFor = IOException.class,
            timeout = 30
    )
    public EmployeeResponseFile saveEmployeeWithPhoto(
            EmployeeFileRequest employeeRequest,
            MultipartFile photo
    ) throws IOException {

        // 1. Check duplicate email/mobile
        Optional<Employee> existingEmp =
                repository.findByEmailOrMobile(
                        employeeRequest.getEmail(),
                        employeeRequest.getMobile()
                );

        if (existingEmp.isPresent()) {

            Employee existing = existingEmp.get();

            boolean emailDuplicate =
                    existing.getEmail()
                            .equalsIgnoreCase(employeeRequest.getEmail());

            boolean mobileDuplicate =
                    existing.getMobile()
                            .equals(employeeRequest.getMobile());

            if (emailDuplicate && mobileDuplicate) {

                throw new GenericException(
                        "Both email and mobile number already exist"
                );

            } else if (emailDuplicate) {

                throw new GenericException(
                        "Email already exists"
                );

            } else {

                throw new GenericException(
                        "Mobile number already exists"
                );
            }
        }


        // 2. Find Department
        Department department = departmentRepository
                .findById(employeeRequest.getDeptId())
                .orElseThrow(() ->
                        new GenericException(
                                "Department not found with id: "
                                        + employeeRequest.getDeptId()
                        )
                );


        // 3. DTO → Employee
        Employee employee =
                EmployeeMapper.dtoToEntityFile(employeeRequest);


        // 4. Set Department
        employee.setDepartment(department);


        // 5. Save Employee
        Employee savedEmployee =
                repository.save(employee);


        // 6. Create EmployeeFile
        EmployeeFile employeeFile =
                new EmployeeFile();

        employeeFile.setFileName(
                photo.getOriginalFilename()
        );

        employeeFile.setContentType(
                photo.getContentType()
        );

        employeeFile.setFileSize(
                photo.getSize()
        );

        employeeFile.setFileData(
                photo.getBytes()
        );

        employeeFile.setFileType("PHOTO");


        // 7. Set relationship
        employeeFile.setEmployee(savedEmployee);


        // 8. Add file to employee collection
        savedEmployee.getFiles().add(employeeFile);


        // 9. Save file
        employeeFileRepository.save(employeeFile);


        // 10. Return response
        return EmployeeMapper.entityToDtoFile(savedEmployee);
    }



}
