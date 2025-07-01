package com.pksa.qrservice.exception;


import com.pksa.qrservice.model.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QRException.class)
    public ResponseEntity<ApiResponse<String>> handleQRException(QRException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), null, false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>("Internal Server Error", null, false);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
