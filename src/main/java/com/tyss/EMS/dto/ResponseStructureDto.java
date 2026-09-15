package com.tyss.EMS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseStructureDto {

    private boolean error;

    private String message;

    private Object data;
}
