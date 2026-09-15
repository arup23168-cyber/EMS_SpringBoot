package com.tyss.EMS.util;

import com.tyss.EMS.dto.*;
import com.tyss.EMS.entity.Address;
import com.tyss.EMS.entity.Department;
import com.tyss.EMS.entity.Employee;
import com.tyss.EMS.entity.EmployeeFile;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {

    //Entity->DTO
    public static EmployeeResponse entityToDto(Employee employee) {
        EmployeeResponse dto = new EmployeeResponse();
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setMobile(employee.getMobile());
        dto.setSalary(employee.getSalary());
        dto.setCasualLeaveBal(employee.getCasualLeaveBal());
        dto.setSickLeaveBal(employee.getSickLeaveBal());
        dto.setUnpaidLeaveBal(employee.getUnpaidLeaveBal());

        //set the Address response
        AddressResponse addressResponse = new AddressResponse();
        Address address = employee.getAddress();

        addressResponse.setStreet(address.getStreet());
        addressResponse.setCity(address.getCity());
        addressResponse.setState(address.getState());
        addressResponse.setPincode(address.getPincode());

        dto.setAddress(address);
        return dto;
    }

    public static Employee dtoToEntity(EmployeeRequest employeeDTO) {

        Employee employee = new Employee();

        //Address to DTO mapping
        Address address = new Address();
        AddressRequest dtoAddress = employeeDTO.getAddress();

        address.setStreet(dtoAddress.getStreet());
        address.setCity(dtoAddress.getCity());
        address.setState(dtoAddress.getState());
        address.setPincode(dtoAddress.getPincode());

        employee.setEmail(employeeDTO.getEmail());
        employee.setName(employeeDTO.getName());
        employee.setMobile(employeeDTO.getMobile());
        employee.setSalary(employeeDTO.getSalary());
        employee.setPassword(employeeDTO.getPassword());

        employee.setAddress(address);


        return employee;
    }

    //for DTO->Entity in v2
    public static Employee dtoToEntityV2(EmployeeV2Request employeeRequest) {
        if (employeeRequest == null) {
            return null;
        }
        Employee employeeEntity = new Employee();

        //Address dto to entity conversion
        Address addressEntity = new Address();

        AddressRequest addressDto = employeeRequest.getAddressDto();

        addressEntity.setStreet(addressDto.getStreet());
        addressEntity.setCity(addressDto.getCity());
        addressEntity.setState(addressDto.getState());
        addressEntity.setPincode(addressDto.getPincode());

        employeeEntity.setName(employeeRequest.getName());
        employeeEntity.setEmail(employeeRequest.getEmail());
        employeeEntity.setMobile(employeeRequest.getMobile());
        employeeEntity.setPassword(employeeRequest.getPassword());
        employeeEntity.setAddress(addressEntity);

        Department departmentEntity = new Department();
        departmentEntity.setId(employeeRequest.getDeptId());

        employeeEntity.setDepartment(departmentEntity);

        return employeeEntity;
    }

    //for ENtity->DTO in v2
    public static EmployeeV2Response entityToDtoV2(Employee employeeEntity) {
        if (employeeEntity == null) {
            return null;
        }
        Address address = employeeEntity.getAddress();
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setStreet(address.getStreet());
        addressResponse.setCity(addressResponse.getCity());
        addressResponse.setState(addressResponse.getState());
        addressResponse.setPincode(address.getPincode());

        Department department = employeeEntity.getDepartment();


        return EmployeeV2Response.builder()
                .name(employeeEntity.getName())
                .email(employeeEntity.getEmail())
                .mobileNo(employeeEntity.getMobile())
                .addressDto(addressResponse)
                .departmentResponseDto(DepartmentResponseDto.builder()
                        .departmentName(department.getDeptName())


                        .build())
                .casualLeaveBal(employeeEntity.getCasualLeaveBal())
                .sickLeaveBal(employeeEntity.getSickLeaveBal())
                .unpaidLeaveBal(employeeEntity.getUnpaidLeaveBal())
                .build();
    }

    public static List<EmployeeResponse> entityToListDto(List<Employee> employee) {
        List<EmployeeResponse> dtoList = new ArrayList<>();
        for (Employee emp : employee) {
            dtoList.add(entityToDto(emp));
        }
        return dtoList;
    }

    public static void updateEntityFromDto(EmployeeUpdate dto, Employee entity) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            entity.setName(dto.getName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getMobile() != null && !dto.getMobile().isBlank()) {
            entity.setMobile(dto.getMobile());
        }
        if (dto.getSalary() != null) {
            entity.setSalary(dto.getSalary());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPassword(dto.getPassword());
        }
    }


    //from DTO->Entity for EmployeeResponseFile
    public static Employee dtoToEntityFile(EmployeeFileRequest dto) {

        Employee employee = new Employee();

        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setMobile(dto.getMobile());
        employee.setSalary(dto.getSalary());
        employee.setPassword(dto.getPassword());

        // Address
        if (dto.getAddress() != null) {

            AddressRequest addressDto = dto.getAddress();

            Address address = new Address();

            address.setStreet(addressDto.getStreet());
            address.setCity(addressDto.getCity());
            address.setState(addressDto.getState());
            address.setPincode(addressDto.getPincode());

            employee.setAddress(address);
        }

        return employee;
    }


    //from Entity->DTO for EmployeeResponseFile
    public static EmployeeResponseFile entityToDtoFile(Employee employee) {

        EmployeeResponseFile dto = new EmployeeResponseFile();

        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setMobile(employee.getMobile());
        dto.setSalary(employee.getSalary());

        // Address
        dto.setAddress(employee.getAddress());

        // Leave balances
        dto.setSickLeaveBal(employee.getSickLeaveBal());
        dto.setCasualLeaveBal(employee.getCasualLeaveBal());
        dto.setUnpaidLeaveBal(employee.getUnpaidLeaveBal());

        // Department
        if (employee.getDepartment() != null) {

            Department department = employee.getDepartment();

            dto.setDepartmentId(department.getId());
            dto.setDepartmentName(department.getDeptName());
        }

        // Files
        List<EmployeeFileResponse> fileResponses = new ArrayList<>();

        if (employee.getFiles() != null) {

            for (EmployeeFile file : employee.getFiles()) {

                EmployeeFileResponse fileDto =
                        new EmployeeFileResponse();

                fileDto.setId(file.getId());
                fileDto.setFileName(file.getFileName());
                fileDto.setContentType(file.getContentType());
                fileDto.setFileSize(file.getFileSize());
                fileDto.setFileType(file.getFileType());

                fileResponses.add(fileDto);
            }
        }

        dto.setFiles(fileResponses);

        return dto;
    }
}
