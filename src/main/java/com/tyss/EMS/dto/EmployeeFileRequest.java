package com.tyss.EMS.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeFileRequest {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50,
            message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Mobile number cannot be blank")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must be 10 digits"
    )
    private String mobile;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than zero")
    private Double salary;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Valid
    private AddressRequest address;

    @NotNull(message = "Department ID is required")
    @Positive(message = "Department ID must be positive")
    private Integer deptId;
}