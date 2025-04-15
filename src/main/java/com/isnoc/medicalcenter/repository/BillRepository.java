package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByVisitVisitId(Long visitId);


     @Query("SELECT b FROM Bill b LEFT JOIN FETCH b.items WHERE b.billId = :billId")
     Optional<Bill> findByIdFetchingItems(@Param("billId") Long billId);

     @Query("SELECT b FROM Bill b LEFT JOIN FETCH b.items WHERE b.visit.visitId = :visitId")
     Optional<Bill> findByVisitVisitIdFetchingItems(@Param("visitId") Long visitId);
}