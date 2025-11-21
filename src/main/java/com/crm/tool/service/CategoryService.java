package com.crm.tool.service;

import com.crm.tool.POJO.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public interface CategoryService {


    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap);

}
