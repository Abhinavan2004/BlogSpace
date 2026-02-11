package com.example.demo.controller;

import com.example.demo.domain.dtos.Dto_Category;
import com.example.demo.domain.dtos.Dto_Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Repository
@Slf4j
@ControllerAdvice
public class Controller_Error {

    @ExceptionHandler(value=Exception.class)
    public ResponseEntity<Dto_Error> handlesException(Exception ex){
        log.error(ex.getMessage(),ex);

        Dto_Error error = Dto_Error.builder()
                .status(HttpStatus.CREATED.value())
                .message("EXception is Founded here").build();
        
        return new ResponseEntity<>(error , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
