package com.isnoc.medicalcenter.controller;

import com.isnoc.medicalcenter.dto.StatisticsDTO;
import com.isnoc.medicalcenter.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for providing overall system statistics
 */
@RestController
@RequestMapping("/stats")
public class StatisticsController {
    
    private final StatisticsService statisticsService;
    
    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }
    
    /**
     * Get comprehensive dashboard statistics
     */
    @GetMapping("/dashboard")
    public ResponseEntity<StatisticsDTO> getDashboardStatistics() {
        return ResponseEntity.ok(statisticsService.getDashboardStatistics());
    }
}
