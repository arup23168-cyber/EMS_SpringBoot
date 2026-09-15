package com.tyss.EMS.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeePageResponse {

    private List<EmployeeResponse> content;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;
}
