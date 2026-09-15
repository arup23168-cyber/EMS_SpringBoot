package com.tyss.EMS.repository;


import com.tyss.EMS.dto.EmployeeResponse;
import com.tyss.EMS.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    Optional<Employee> findByEmailOrMobile(String email, String mobile);

    Page<Employee> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);

    @Query("""
SELECT e FROM Employee e LEFT JOIN FETCH e.address""")
    public List<Employee> findAllWithAddress();


//    @Override
//    @EntityGraph(attributePaths = "address")
//    public List<Employee> findAll();

    // ✅ Custom name → @Query needed
//    @Query("SELECT e FROM Employee e")
//    @EntityGraph(attributePaths = "address")
//    List<Employee> findAllEmployees();

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);
}

