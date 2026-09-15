package com.tyss.EMS.service;

import com.tyss.EMS.dto.LeavePageResponse;
import com.tyss.EMS.dto.LeaveRequestDto;
import com.tyss.EMS.dto.LeaveResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaveService {
    LeaveResponseDto applyLeave(LeaveRequestDto leaveRequestDto);

    LeavePageResponse getAllLeaves(String search, Pageable pageable);

    LeaveResponseDto leaveAction(Integer id, String action);

    LeaveResponseDto deleteparticularLeave(Integer id,Integer empId);
}
