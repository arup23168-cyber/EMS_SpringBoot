package com.tyss.EMS.service;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeBatchService {

    private final JobOperator jobOperator;
    private final Job employeeJob;

    public void importEmployees() throws Exception {

        JobParameters jobParameters =
                new JobParameters();

        jobOperator.start(
                employeeJob,
                jobParameters
        );
    }
}