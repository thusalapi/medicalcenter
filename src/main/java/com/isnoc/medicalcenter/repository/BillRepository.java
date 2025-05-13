package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByVisitVisitId(Long visitId);


    @Query("SELECT b FROM Bill b LEFT JOIN FETCH b.items WHERE b.billId = :billId")
    Optional<Bill> findByIdFetchingItems(@Param("billId") Long billId);

    @Query("SELECT b FROM Bill b LEFT JOIN FETCH b.items WHERE b.visit.visitId = :visitId")
    Optional<Bill> findByVisitVisitIdFetchingItems(@Param("visitId") Long visitId);
    
    // Statistics queries for dashboard
    @Query("SELECT SUM(b.totalAmount) FROM Bill b")
    BigDecimal sumTotalAmount();    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Bill b WHERE b.billDate >= :startOfMonth AND b.billDate < :startOfNextMonth")
    BigDecimal sumTotalAmountForCurrentMonth(@Param("startOfMonth") LocalDateTime startOfMonth, @Param("startOfNextMonth") LocalDateTime startOfNextMonth);
    
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Bill b WHERE b.billDate >= :startOfDay AND b.billDate < :startOfNextDay")
    BigDecimal sumTotalAmountForToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("startOfNextDay") LocalDateTime startOfNextDay);
}