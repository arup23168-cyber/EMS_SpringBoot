package com.tyss.EMS.repository;

import com.tyss.EMS.entity.EmployeeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeFileRepository
        extends JpaRepository<EmployeeFile, Integer> {

    List<EmployeeFile> findByEmployeeId(Integer employeeId);
}