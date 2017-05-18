package com.sedatcan.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
@Builder
public class ToDoListException extends RuntimeException {
    private ToDoListErrorCode errorCode;
    private HttpStatus httpStatus;
}
