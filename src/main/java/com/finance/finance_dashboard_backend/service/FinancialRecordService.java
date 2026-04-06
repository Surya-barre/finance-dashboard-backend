package com.finance.finance_dashboard_backend.service;

import com.finance.finance_dashboard_backend.entity.FinancialRecord;
import com.finance.finance_dashboard_backend.enums.RecordType;
import com.finance.finance_dashboard_backend.exception.CustomException;
import com.finance.finance_dashboard_backend.repository.FinancialRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
// Handles business logic for financial records
public class FinancialRecordService {

    @Autowired
    private FinancialRecordRepository repository;

    // Create new record
    public FinancialRecord createRecord(FinancialRecord record) {
        return repository.save(record);
    }




    public Page<FinancialRecord> getAllRecords(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.findAll(pageable);
    }

    // Get record by ID (common method)
    private FinancialRecord getRecordOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("Record not found"));
    }

    // Full update
    public FinancialRecord updateRecord(Long id, FinancialRecord record) {

        FinancialRecord existing = getRecordOrThrow(id);

        existing.setAmount(record.getAmount());
        existing.setType(record.getType());
        existing.setCategory(record.getCategory());
        existing.setDate(record.getDate());
        existing.setNote(record.getNote());

        return repository.save(existing);
    }

    // Partial update
    public FinancialRecord patchRecord(Long id, FinancialRecord record) {

        FinancialRecord existing = getRecordOrThrow(id);

        if (record.getAmount() != null) {
            existing.setAmount(record.getAmount());
        }

        if (record.getType() != null) {
            existing.setType(record.getType());
        }

        if (record.getCategory() != null) {
            existing.setCategory(record.getCategory());
        }

        if (record.getDate() != null) {
            existing.setDate(record.getDate());
        }

        if (record.getNote() != null) {
            existing.setNote(record.getNote());
        }

        return repository.save(existing);
    }

    // Delete record
    public void deleteRecord(Long id) {
        FinancialRecord existing = getRecordOrThrow(id);
        repository.delete(existing);
    }


    // filter without pagination
    public List<FinancialRecord> filterRecordsWithoutPagination(
            RecordType type, String category, LocalDate date) {

        List<FinancialRecord> result = new ArrayList<>();

        for (FinancialRecord record : repository.findAll()) {

            boolean match = true;

            if (type != null && record.getType() != type) match = false;
            if (category != null && !record.getCategory().equalsIgnoreCase(category)) match = false;
            if (date != null && !record.getDate().equals(date)) match = false;

            if (match) result.add(record);
        }

        return result;
    }


    public Page<FinancialRecord> filterRecords(
            RecordType type,
            String category,
            LocalDate date,
            int page,
            int size) {

        List<FinancialRecord> records = repository.findAll();
        List<FinancialRecord> filtered = new ArrayList<>();

        for (FinancialRecord record : records) {

            boolean match = true;

            if (type != null && record.getType() != type) {
                match = false;
            }

            if (category != null && !record.getCategory().equalsIgnoreCase(category)) {
                match = false;
            }

            if (date != null && !record.getDate().equals(date)) {
                match = false;
            }

            if (match) {
                filtered.add(record);
            }
        }

        // pagination logic
        int start = page * size;
        int end = Math.min(start + size, filtered.size());

        List<FinancialRecord> pageContent = filtered.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), filtered.size());
    }

    // Summary (income, expense, balance)
    public Map<String, Double> getSummary() {

        List<FinancialRecord> records = repository.findAll();

        double income = records.stream()
                .filter(r -> r.getType() == RecordType.INCOME)
                .mapToDouble(FinancialRecord::getAmount)
                .sum();

        double expense = records.stream()
                .filter(r -> r.getType() == RecordType.EXPENSE)
                .mapToDouble(FinancialRecord::getAmount)
                .sum();

        Map<String, Double> result = new HashMap<>();
        result.put("totalIncome", income);
        result.put("totalExpense", expense);
        result.put("netBalance", income - expense);

        return result;
    }

    // Category-wise summary
    public Map<String, Double> getCategorySummary() {

        List<FinancialRecord> records = repository.findAll();

        Map<String, Double> result = new HashMap<>();

        for (FinancialRecord record : records) {
            String category = record.getCategory();

            result.put(
                    category,
                    result.getOrDefault(category, 0.0) + record.getAmount()
            );
        }

        return result;
    }

    // Recent 5 records
    public List<FinancialRecord> getRecentRecords() {
        return repository.findTop5ByOrderByDateDesc();
    }

    // Monthly summary
    public Map<Integer, Double> getMonthlySummary() {

        List<FinancialRecord> records = repository.findAll();

        Map<Integer, Double> result = new HashMap<>();

        for (FinancialRecord record : records) {
            int month = record.getDate().getMonthValue();

            result.put(
                    month,
                    result.getOrDefault(month, 0.0) + record.getAmount()
            );
        }

        return result;
    }
}
