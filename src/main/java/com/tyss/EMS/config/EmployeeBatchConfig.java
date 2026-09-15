package com.tyss.EMS.config;

import com.tyss.EMS.dto.EmployeeCsvDto;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class EmployeeBatchConfig {

    @Bean
    public FlatFileItemReader<EmployeeCsvDto> employeeReader() {

        return new FlatFileItemReaderBuilder<EmployeeCsvDto>()
                .name("employeeReader")
                .resource(new FileSystemResource("employees.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("name", "email", "mobile")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(EmployeeCsvDto.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<EmployeeCsvDto, EmployeeCsvDto> employeeProcessor() {

        return employee -> {
            // Add processing logic here
            return employee;
        };
    }

    @Bean
    public ItemWriter<EmployeeCsvDto> employeeWriter() {

        return chunk -> {
            for (EmployeeCsvDto employee : chunk) {
                System.out.println(
                        employee.getName() + " "
                                + employee.getEmail() + " "
                                + employee.getMobile()
                );
            }
        };
    }

    @Bean
    public Step employeeStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<EmployeeCsvDto> employeeReader,
            ItemProcessor<EmployeeCsvDto, EmployeeCsvDto> employeeProcessor,
            ItemWriter<EmployeeCsvDto> employeeWriter) {

        return new StepBuilder("employeeStep", jobRepository)
                .<EmployeeCsvDto, EmployeeCsvDto>chunk(10)
                .reader(employeeReader)
                .processor(employeeProcessor)
                .writer(employeeWriter)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Job employeeJob(
            JobRepository jobRepository,
            Step employeeStep) {

        return new JobBuilder("employeeJob", jobRepository)
                .start(employeeStep)
                .build();
    }
}