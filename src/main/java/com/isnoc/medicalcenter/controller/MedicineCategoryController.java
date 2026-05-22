package com.isnoc.medicalcenter.controller;

import com.isnoc.medicalcenter.dto.MedicineCategoryDTO;
import com.isnoc.medicalcenter.dto.ResponseDTO;
import com.isnoc.medicalcenter.service.MedicineCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicine-categories")
public class MedicineCategoryController {

    private final MedicineCategoryService categoryService;

    @Autowired
    public MedicineCategoryController(MedicineCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<MedicineCategoryDTO>>> getAllCategories() {
        List<MedicineCategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ResponseDTO.success(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<MedicineCategoryDTO>> getCategoryById(@PathVariable Long id) {
        MedicineCategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ResponseDTO.success(category));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<MedicineCategoryDTO>> createCategory(
            @RequestBody MedicineCategoryDTO categoryDTO) {
        MedicineCategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(
                ResponseDTO.success("Category created successfully", createdCategory),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<MedicineCategoryDTO>> updateCategory(
            @PathVariable Long id, 
            @RequestBody MedicineCategoryDTO categoryDTO) {
        MedicineCategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(
                ResponseDTO.success("Category updated successfully", updatedCategory)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                ResponseDTO.success("Category deleted successfully", null)
        );
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<ResponseDTO<MedicineCategoryDTO>> getCategoryByName(@PathVariable String name) {
        MedicineCategoryDTO category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(ResponseDTO.success(category));
    }
}
