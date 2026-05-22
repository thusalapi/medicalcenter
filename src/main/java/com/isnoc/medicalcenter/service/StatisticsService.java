package com.isnoc.medicalcenter.service;

import com.isnoc.medicalcenter.dto.StatisticsDTO;

/**
 * Service interface for statistics data
 */
public interface StatisticsService {
    
    /**
     * Get combined statistics for dashboard
     */
    StatisticsDTO getDashboardStatistics();
    
    /**
     * Get visit-specific statistics
     */
    StatisticsDTO getVisitStatistics();
    
    /**
     * Get billing-specific statistics
     */
    StatisticsDTO getBillingStatistics();
    
    /**
     * Get report-specific statistics
     */
    StatisticsDTO getReportStatistics();
}
