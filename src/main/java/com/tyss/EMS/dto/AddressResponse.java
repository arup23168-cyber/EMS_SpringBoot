package com.tyss.EMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {

    private String street;

    private String city;

    private String state;

    private int pincode;
}
