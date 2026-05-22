package com.isnoc.medicalcenter.service.impl;

import com.isnoc.medicalcenter.dto.StatisticsDTO;
import com.isnoc.medicalcenter.repository.BillRepository;
import com.isnoc.medicalcenter.repository.ReportRepository;
import com.isnoc.medicalcenter.repository.VisitRepository;
import com.isnoc.medicalcenter.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Implementation of StatisticsService for analytics dashboard
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    
    private final VisitRepository visitRepository;
    private final BillRepository billRepository;
    private final ReportRepository reportRepository;
    
    @Autowired
    public StatisticsServiceImpl(
            VisitRepository visitRepository,
            BillRepository billRepository,
            ReportRepository reportRepository) {
        this.visitRepository = visitRepository;
        this.billRepository = billRepository;
        this.reportRepository = reportRepository;
    }
    
    @Override
    public StatisticsDTO getDashboardStatistics() {
        // Get comprehensive statistics for dashboard
        StatisticsDTO stats = new StatisticsDTO();
        
        // Visit statistics
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        Long totalVisits = visitRepository.count();
        Long visitsToday = visitRepository.countByVisitDateBetween(startOfDay, endOfDay);
          // Bill statistics
        BigDecimal totalRevenue = billRepository.sumTotalAmount();
        
        // Calculate start and end of current month for monthly revenue
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate firstDayOfNextMonth = today.plusMonths(1).withDayOfMonth(1);
        LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
        LocalDateTime startOfNextMonth = firstDayOfNextMonth.atStartOfDay();
        BigDecimal monthlyRevenue = billRepository.sumTotalAmountForCurrentMonth(startOfMonth, startOfNextMonth);
        
        // Calculate start and end of today for today's revenue
        BigDecimal todayRevenue = billRepository.sumTotalAmountForToday(startOfDay, endOfDay);
        
        // Report statistics
        Long totalReports = reportRepository.count();
        
        // Set all statistics
        stats.setTotalVisits(totalVisits.intValue());
        stats.setVisitsToday(visitsToday.intValue());
        stats.setTotalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        stats.setMonthlyRevenue(monthlyRevenue != null ? monthlyRevenue : BigDecimal.ZERO);
        stats.setTodayRevenue(todayRevenue != null ? todayRevenue : BigDecimal.ZERO);
        stats.setTotalReports(totalReports.intValue());
        
        return stats;
    }
    
    @Override
    public StatisticsDTO getVisitStatistics() {
        StatisticsDTO stats = new StatisticsDTO();
        
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        Long totalVisits = visitRepository.count();
        Long visitsToday = visitRepository.countByVisitDateBetween(startOfDay, endOfDay);
        
        stats.setTotalVisits(totalVisits.intValue());
        stats.setVisitsToday(visitsToday.intValue());
        
        return stats;
    }
    
    @Override
    public StatisticsDTO getBillingStatistics() {
        StatisticsDTO stats = new StatisticsDTO();
          LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        BigDecimal totalRevenue = billRepository.sumTotalAmount();
        
        // Calculate start and end of current month for monthly revenue
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate firstDayOfNextMonth = today.plusMonths(1).withDayOfMonth(1);
        LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
        LocalDateTime startOfNextMonth = firstDayOfNextMonth.atStartOfDay();
        BigDecimal monthlyRevenue = billRepository.sumTotalAmountForCurrentMonth(startOfMonth, startOfNextMonth);
        
        // Calculate start and end of today for today's revenue
        BigDecimal todayRevenue = billRepository.sumTotalAmountForToday(startOfDay, endOfDay);
        
        stats.setTotalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        stats.setMonthlyRevenue(monthlyRevenue != null ? monthlyRevenue : BigDecimal.ZERO);
        stats.setTodayRevenue(todayRevenue != null ? todayRevenue : BigDecimal.ZERO);
        
        return stats;
    }
    
    @Override
    public StatisticsDTO getReportStatistics() {
        StatisticsDTO stats = new StatisticsDTO();
        
        Long totalReports = reportRepository.count();
        // Assuming pending reports are those that have been created today
        LocalDate today = LocalDate.now();
        Long pendingReports = reportRepository.countByCreatedDateAfter(today.atStartOfDay());
        
        stats.setTotalReports(totalReports.intValue());
        stats.setPendingReports(pendingReports.intValue());
        
        return stats;
    }
}
