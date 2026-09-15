package com.tyss.EMS.repository;

import com.tyss.EMS.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {

}
