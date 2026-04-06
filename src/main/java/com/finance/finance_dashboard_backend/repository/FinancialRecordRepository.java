package com.finance.finance_dashboard_backend.repository;

import com.finance.finance_dashboard_backend.entity.FinancialRecord;
import com.finance.finance_dashboard_backend.enums.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

// Repository for FinancialRecord database operations
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // Get records by type and category
    List<FinancialRecord> findByTypeAndCategory(RecordType type, String category);

    // Get records by date
    List<FinancialRecord> findByDate(LocalDate date);

    // Get latest 5 records (recent transactions)
    List<FinancialRecord> findTop5ByOrderByDateDesc();
}