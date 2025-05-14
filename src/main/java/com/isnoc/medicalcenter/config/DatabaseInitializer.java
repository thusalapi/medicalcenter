package com.isnoc.medicalcenter.config;

import com.isnoc.medicalcenter.entity.MedicineCategory;
import com.isnoc.medicalcenter.repository.MedicineCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DatabaseInitializer {    @Bean
    public CommandLineRunner initializeDatabase(MedicineCategoryRepository categoryRepository) {
        return args -> {
            // Initialize default medicine categories if none exist
            if (categoryRepository.count() == 0) {                List<MedicineCategory> defaultCategories = new java.util.ArrayList<>();                // Create medicine categories using setter methods
                MedicineCategory antibiotics = new MedicineCategory();
                antibiotics.setName("Antibiotics");
                antibiotics.setDescription("Medicines that kill or inhibit the growth of bacteria");
                defaultCategories.add(antibiotics);
                
                MedicineCategory analgesics = new MedicineCategory();
                analgesics.setName("Analgesics");
                analgesics.setDescription("Pain relieving medications");
                defaultCategories.add(analgesics);
                
                MedicineCategory antacids = new MedicineCategory();
                antacids.setName("Antacids");
                antacids.setDescription("Medicines that neutralize stomach acidity");
                defaultCategories.add(antacids);
                
                MedicineCategory antihistamines = new MedicineCategory();
                antihistamines.setName("Antihistamines");
                antihistamines.setDescription("Medicines used to treat allergies");
                defaultCategories.add(antihistamines);
                
                MedicineCategory antihypertensives = new MedicineCategory();
                antihypertensives.setName("Antihypertensives");
                antihypertensives.setDescription("Medicines used to treat high blood pressure");
                defaultCategories.add(antihypertensives);
                
                MedicineCategory antipyretics = new MedicineCategory();
                antipyretics.setName("Antipyretics");
                antipyretics.setDescription("Fever reducing medications");
                defaultCategories.add(antipyretics);
                
                MedicineCategory others = new MedicineCategory();
                others.setName("Others");
                others.setDescription("Other types of medicines");
                defaultCategories.add(others);
                
                categoryRepository.saveAll(defaultCategories);
                System.out.println("Initialized default medicine categories.");
            }
        };
    }
}
