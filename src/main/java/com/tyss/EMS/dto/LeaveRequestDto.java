package com.tyss.EMS.dto;

import com.tyss.EMS.entity.LeaveType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LeaveRequestDto {

    private Integer empId;

    private LocalDate fromDate;

    private LocalDate toDate;

    private LeaveType leaveType;

    private String reason;
}
