package com.isnoc.medicalcenter.dto;

import java.math.BigDecimal;

/**
 * DTO class for returning statistics data to the frontend
 */
public class StatisticsDTO {
    
    // Visit statistics
    private Integer totalVisits;
    private Integer visitsToday;
    
    // Billing statistics
    private BigDecimal totalRevenue;
    private BigDecimal monthlyRevenue;
    private BigDecimal todayRevenue;
    
    // Report statistics
    private Integer totalReports;
    private Integer pendingReports;
    
    // Default constructor
    public StatisticsDTO() {
    }
    
    // Getters and setters
    public Integer getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(Integer totalVisits) {
        this.totalVisits = totalVisits;
    }

    public Integer getVisitsToday() {
        return visitsToday;
    }

    public void setVisitsToday(Integer visitsToday) {
        this.visitsToday = visitsToday;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public BigDecimal getTodayRevenue() {
        return todayRevenue;
    }

    public void setTodayRevenue(BigDecimal todayRevenue) {
        this.todayRevenue = todayRevenue;
    }

    public Integer getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(Integer totalReports) {
        this.totalReports = totalReports;
    }

    public Integer getPendingReports() {
        return pendingReports;
    }

    public void setPendingReports(Integer pendingReports) {
        this.pendingReports = pendingReports;
    }
}
