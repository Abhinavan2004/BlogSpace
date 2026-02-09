package com.example.demo.controller;

import com.example.demo.domain.entity.Entity_Category;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class Controller_Category {
    private final Service_category serviceCategory;

    public List<List<Dto_Category>> categoryList() {
        List<Entity_Category> entityCategoryList = new ArrayList<>();
        return entityCategoryList ;
    }
}
