package com.isnoc.medicalcenter.service;

import com.isnoc.medicalcenter.dto.MedicineCategoryDTO;
import com.isnoc.medicalcenter.entity.MedicineCategory;
import com.isnoc.medicalcenter.repository.MedicineCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class MedicineCategoryService {

    private final MedicineCategoryRepository categoryRepository;

    @Autowired
    public MedicineCategoryService(MedicineCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<MedicineCategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(MedicineCategoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public MedicineCategoryDTO getCategoryById(Long id) {
        MedicineCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Medicine category not found with id: " + id));
        return MedicineCategoryDTO.fromEntity(category);
    }

    @Transactional
    public MedicineCategoryDTO createCategory(MedicineCategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name " + categoryDTO.getName() + " already exists");
        }
        
        MedicineCategory category = categoryDTO.toEntity();
        category.setCategoryId(null); // Ensure we're creating a new entity
        
        MedicineCategory savedCategory = categoryRepository.save(category);
        return MedicineCategoryDTO.fromEntity(savedCategory);
    }

    @Transactional
    public MedicineCategoryDTO updateCategory(Long id, MedicineCategoryDTO categoryDTO) {
        MedicineCategory existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Medicine category not found with id: " + id));
        
        // Check if name is changed and if the new name already exists
        if (!existingCategory.getName().equals(categoryDTO.getName()) && 
                categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name " + categoryDTO.getName() + " already exists");
        }
        
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());
        
        MedicineCategory updatedCategory = categoryRepository.save(existingCategory);
        return MedicineCategoryDTO.fromEntity(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NoSuchElementException("Medicine category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
        // Note: This might throw an exception if there are medicines still using this category
    }

    public MedicineCategoryDTO getCategoryByName(String name) {
        MedicineCategory category = categoryRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Medicine category not found with name: " + name));
        return MedicineCategoryDTO.fromEntity(category);
    }
}
