package com.tyss.EMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeFileResponse {

    private Integer id;

    private String fileName;

    private String contentType;

    private Long fileSize;

    private String fileType;
}