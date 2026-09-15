package com.tyss.EMS.util;

import com.tyss.EMS.dto.LeaveRequestDto;
import com.tyss.EMS.dto.LeaveResponseDto;
import com.tyss.EMS.entity.Leave;
import org.springframework.stereotype.Component;

@Component
public class LeaveMapper {

    // Request DTO → Entity
    public static Leave dtoToEntity(LeaveRequestDto dto) {

        Leave leave = new Leave();

        leave.setFromDate(dto.getFromDate());
        leave.setToDate(dto.getToDate());
        leave.setLeaveType(dto.getLeaveType());
        leave.setReason(dto.getReason());

        return leave;
    }

    // Entity → Response DTO
    public static  LeaveResponseDto entityToResponseDto(Leave leave) {

        LeaveResponseDto dto = new LeaveResponseDto();

        dto.setLeaveID(leave.getId());
        dto.setEmpId(leave.getEmployee().getId());
        dto.setFromDate(leave.getFromDate());
        dto.setToDate(leave.getToDate());
        dto.setLeaveType(leave.getLeaveType());
        dto.setReason(leave.getReason());
        dto.setStatus(leave.getStatus());

        return dto;
    }
}