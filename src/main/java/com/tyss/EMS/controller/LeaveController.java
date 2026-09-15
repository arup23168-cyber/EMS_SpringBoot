package com.tyss.EMS.controller;

import com.tyss.EMS.dto.LeavePageResponse;
import com.tyss.EMS.dto.LeaveRequestDto;
import com.tyss.EMS.dto.LeaveResponseDto;
import com.tyss.EMS.dto.ResponseStructureDto;
import com.tyss.EMS.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping
    public ResponseStructureDto applyLeave(@RequestBody LeaveRequestDto leaveRequestDto){
        LeaveResponseDto leaveResponseDto = leaveService.applyLeave(leaveRequestDto);

        return ResponseStructureDto.builder()
                .error(false)
                .message("Leave applied successfully")
                .data(leaveRequestDto)
                .build();
    }

    @GetMapping
    public LeavePageResponse getAllLeaves(@RequestParam(required = false) String search,
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "id",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {

        return leaveService.getAllLeaves(search, pageable);
    }

    @PutMapping("/{id}/action")
    public LeaveResponseDto leaveAction(@PathVariable Integer id, @RequestParam String action) {

        return leaveService.leaveAction(id, action);
    }

    @DeleteMapping("/{id}/emp/{empId}")
    public LeaveResponseDto deleteTheLeave(@PathVariable Integer id,@PathVariable Integer empId){
        return leaveService.deleteparticularLeave(id,empId);
    }
}
