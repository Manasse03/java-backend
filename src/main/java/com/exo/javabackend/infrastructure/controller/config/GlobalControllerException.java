package com.exo.javabackend.infrastructure.controller.config;


import com.exo.javabackend.domain.exception.BadRequestException;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerException {

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().build();
    }

}
