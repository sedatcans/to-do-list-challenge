package com.sedatcan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BaseResponse {
    private BaseError error;

    public BaseResponse buildBaseError(String message, String code) {
        this.error = new BaseError(message, code);
        return this;
    }

    @Data
    @AllArgsConstructor
    private class BaseError {
        private String message;
        private String code;
    }
}
