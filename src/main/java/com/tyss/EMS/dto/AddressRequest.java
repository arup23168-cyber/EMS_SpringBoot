package com.tyss.EMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {

    private String street;

    private String city;

    private String state;

    private int pincode;
}
