package com.tyss.EMS.service;

import com.tyss.EMS.dto.LeavePageResponse;
import com.tyss.EMS.dto.LeaveRequestDto;
import com.tyss.EMS.dto.LeaveResponseDto;
import com.tyss.EMS.entity.Employee;
import com.tyss.EMS.entity.Leave;
import com.tyss.EMS.entity.LeaveStatus;
import com.tyss.EMS.entity.LeaveType;
import com.tyss.EMS.exception.GenericException;
import com.tyss.EMS.repository.EmployeeRepository;
import com.tyss.EMS.repository.LeaveRepository;
import com.tyss.EMS.util.LeaveMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final EmployeeRepository employeeRepository;

    private final LeaveRepository leaveRepository;

    @Override
    @Transactional
    public LeaveResponseDto applyLeave(LeaveRequestDto leaveRequestDto) {

        // 1. Find employee
        Employee employee = employeeRepository
                .findById(leaveRequestDto.getEmpId())
                .orElseThrow(() ->
                        new GenericException("Employee not found"));

        // 2. Validate dates
        if (leaveRequestDto.getFromDate()
                .isAfter(leaveRequestDto.getToDate())) {

            throw new GenericException(
                    "From date cannot be after to date"
            );
        }

        // 3. Calculate number of days
        long numberOfDays = java.time.temporal.ChronoUnit.DAYS.between(
                leaveRequestDto.getFromDate(),
                leaveRequestDto.getToDate()
        ) + 1;

        int days = (int) numberOfDays;

        // 4. Check and deduct balance
        if (leaveRequestDto.getLeaveType() == LeaveType.SICK) {

            if (employee.getSickLeaveBal() < days) {
                throw new GenericException(
                        "Insufficient sick leave balance"
                );
            }

            employee.setSickLeaveBal(
                    employee.getSickLeaveBal() - days
            );

        } else if (leaveRequestDto.getLeaveType() == LeaveType.CASUAL) {

            if (employee.getCasualLeaveBal() < days) {
                throw new GenericException(
                        "Insufficient casual leave balance"
                );
            }

            employee.setCasualLeaveBal(
                    employee.getCasualLeaveBal() - days
            );

        } else if (leaveRequestDto.getLeaveType() == LeaveType.UNPAID) {

            if (employee.getUnpaidLeaveBal() < days) {
                throw new GenericException(
                        "Insufficient unpaid leave balance"
                );
            }

            employee.setUnpaidLeaveBal(
                    employee.getUnpaidLeaveBal() - days
            );
        }

        // 5. Convert DTO → Entity
        Leave leave = LeaveMapper.dtoToEntity(leaveRequestDto);

        // 6. Set employee
        leave.setEmployee(employee);

        // 7. Set status
        leave.setStatus(LeaveStatus.PENDING);

        // 8. Save employee balance
        employeeRepository.save(employee);

        // 9. Save leave
        Leave savedLeave = leaveRepository.save(leave);

        return LeaveMapper.entityToResponseDto(savedLeave);
    }


    @Override
    public LeavePageResponse getAllLeaves(String search, Pageable pageable) {

        Page<Leave> leavePage;

        if (search == null || search.isBlank()) {

            leavePage = leaveRepository.findAll(pageable);

        } else {

            leavePage = leaveRepository.findByStatusContainingIgnoreCase(search, pageable);
        }

        // 3. Convert Leave entities to DTOs
        List<LeaveResponseDto> responseList = new ArrayList<>();

        List<Leave> content = leavePage.getContent();

        for (Leave leave :content) {

            LeaveResponseDto dto =LeaveMapper.entityToResponseDto(leave);

            responseList.add(dto);
        }

        // Create response object
        LeavePageResponse response = new LeavePageResponse();

        response.setLeaveResponseList(responseList);
        response.setPage(leavePage.getNumber());
        response.setSize(leavePage.getSize());
        response.setTotalElements(leavePage.getTotalElements());
        response.setTotalPages(leavePage.getTotalPages());

        return response;
    }

    @Override
    @Transactional
    public LeaveResponseDto leaveAction(Integer id, String action) {

        // 1. Find leave
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() ->
                        new GenericException("Leave not found"));

        // 2. Only pending leave can be actioned
        if (!LeaveStatus.PENDING.equals(leave.getStatus())) {
            throw new GenericException(
                    "Only pending leave can be approved or rejected"
            );
        }

        Employee employee = leave.getEmployee();

        // 3. Calculate days
        long numberOfDays = java.time.temporal.ChronoUnit.DAYS.between(
                leave.getFromDate(),
                leave.getToDate()
        ) + 1;

        int days = (int) numberOfDays;

        // =========================
        // APPROVE
        // =========================

        if (action.equalsIgnoreCase("APPROVE")) {

            // Balance was already deducted during applyLeave()
            // So DON'T deduct again.

            leave.setStatus(LeaveStatus.APPROVED);

        }

        // =========================
        // REJECT
        // =========================

        else if (action.equalsIgnoreCase("REJECT")) {

            // Restore balance
            if (leave.getLeaveType() == LeaveType.SICK) {

                employee.setSickLeaveBal(
                        employee.getSickLeaveBal() + days
                );

            } else if (leave.getLeaveType() == LeaveType.CASUAL) {

                employee.setCasualLeaveBal(
                        employee.getCasualLeaveBal() + days
                );

            } else if (leave.getLeaveType() == LeaveType.UNPAID) {

                employee.setUnpaidLeaveBal(
                        employee.getUnpaidLeaveBal() + days
                );
            }

            leave.setStatus(LeaveStatus.REJECTED);

            employeeRepository.save(employee);
        }

        else {

            throw new GenericException(
                    "Invalid action. Use APPROVE or REJECT"
            );
        }

        Leave savedLeave = leaveRepository.save(leave);

        return LeaveMapper.entityToResponseDto(savedLeave);
    }

    @Override
    @Transactional
    public LeaveResponseDto deleteparticularLeave(Integer id, Integer empId) {

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() ->
                        new GenericException("Employee didn't apply leave"));

        Integer userEmpId = leave.getEmployee().getId();

        if (!userEmpId.equals(empId)) {
            throw new GenericException(
                    "Please login as the correct employee"
            );
        }

        if (!LeaveStatus.PENDING.equals(leave.getStatus())) {
            throw new GenericException(
                    "Only pending leave can be deleted"
            );
        }

        Employee employee = leave.getEmployee();

        long numberOfDays = java.time.temporal.ChronoUnit.DAYS.between(
                leave.getFromDate(),
                leave.getToDate()
        ) + 1;

        int days = (int) numberOfDays;

        // Restore balance
        if (leave.getLeaveType() == LeaveType.SICK) {

            employee.setSickLeaveBal(
                    employee.getSickLeaveBal() + days
            );

        } else if (leave.getLeaveType() == LeaveType.CASUAL) {

            employee.setCasualLeaveBal(
                    employee.getCasualLeaveBal() + days
            );

        } else if (leave.getLeaveType() == LeaveType.UNPAID) {

            employee.setUnpaidLeaveBal(
                    employee.getUnpaidLeaveBal() + days
            );
        }

        employeeRepository.save(employee);

        leaveRepository.delete(leave);

        return LeaveMapper.entityToResponseDto(leave);
    }
}

