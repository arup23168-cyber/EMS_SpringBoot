package com.tyss.EMS.dto;

import com.tyss.EMS.entity.LeaveStatus;
import com.tyss.EMS.entity.LeaveType;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveResponseDto {

    private Integer leaveID;

    private Integer empId;

    private LocalDate fromDate;

    private LocalDate toDate;

    private String reason;

    private LeaveStatus status;

    private LeaveType leaveType;

}
