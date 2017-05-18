package com.sedatcan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ToDoListErrorCode {

    CUSTOMER_EXIST("TODO10000", "Customer has allready been registered."),
    UNAUTHORIZED_REQUEST("TODO10001", "ınvalid Credentials");

    private String errorCode;
    private String errorMessage;
}
