package com.sedatcan.controller;

import com.sedatcan.exception.ToDoListException;
import com.sedatcan.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ToDoListException.class)
    public ResponseEntity<BaseResponse> handleExpectedException(ToDoListException exception, HttpServletResponse httpServletResponse) {
        log.error("Expected exception occurred: ", exception);

        int statusCode = exception.getHttpStatus().value();
        httpServletResponse.setStatus(statusCode);

        return new ResponseEntity<BaseResponse>(BaseResponse.builder().build().buildBaseError(exception.getErrorCode().getErrorMessage(), exception.getErrorCode().getErrorCode()), exception.getHttpStatus());
    }
}
