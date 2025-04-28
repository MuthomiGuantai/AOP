package com.bruceycode.AOP_Demo.model;

import lombok.Data;

@Data
public class ErrorResponse {

    private String errorcode;
    private String message;
    private String details;

}
