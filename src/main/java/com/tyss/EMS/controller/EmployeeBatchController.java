package com.tyss.EMS.controller;

import com.tyss.EMS.service.EmployeeBatchService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeBatchController {

    private final EmployeeBatchService employeeBatchService;

    @PostMapping("/import")
    public ResponseEntity<String> importEmployees() throws Exception {

        employeeBatchService.importEmployees();

        return ResponseEntity.ok("Employee import started");
    }
}