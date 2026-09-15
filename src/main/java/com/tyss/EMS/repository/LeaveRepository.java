package com.tyss.EMS.repository;

import com.tyss.EMS.entity.Leave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Integer> {

    Page<Leave> findByStatusContainingIgnoreCase(String status, Pageable pageable);


}
