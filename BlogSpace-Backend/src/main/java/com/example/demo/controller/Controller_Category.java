package com.example.demo.controller;

import com.example.demo.domain.dtos.Dto_Category;
import com.example.demo.domain.mappers.Category_Mapper;
import com.example.demo.service.Service_Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class Controller_Category {

    private final Service_Category serviceCategory;
    private final Category_Mapper category_mapper;

    @GetMapping
    public ResponseEntity<List<Dto_Category>> categoryList() {
        List<Dto_Category> categories = serviceCategory.listCategories()
                .stream().map(category_mapper::toDto)
                .toList();
        return ResponseEntity.ok(categories);
    }


    @PostMapping
    public ResponseEntity<Dto_C>
}



