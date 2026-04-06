package com.finance.finance_dashboard_backend.controller;

import com.finance.finance_dashboard_backend.entity.FinancialRecord;
import com.finance.finance_dashboard_backend.entity.User;
import com.finance.finance_dashboard_backend.enums.RecordType;
import com.finance.finance_dashboard_backend.enums.Role;
import com.finance.finance_dashboard_backend.exception.CustomException;
import com.finance.finance_dashboard_backend.service.FinancialRecordService;
import com.finance.finance_dashboard_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/records")
// Controller to handle all financial record related APIs
public class FinancialRecordController {

    // Inject FinancialRecordService for business logic
    @Autowired
    private FinancialRecordService service;

    // Inject UserService for user validation
    @Autowired
    private UserService userService;

    // -------- COMMON VALIDATION METHODS --------

    // Validate if user exists and is active
    private User validateUser(Long userId) {
        User user = userService.getUserById(userId);

        if (!user.getActive()) {
            throw new CustomException("User is inactive");
        }

        return user;
    }

    // Validate if user has ADMIN role (used for create/update/delete)
    private void validateAdmin(User user) {
        if (user.getRole() != Role.ADMIN) {
            throw new CustomException("Access denied");
        }
    }

    // Restrict VIEWER role (used for summary APIs)
    private void validateNotViewer(User user) {
        if (user.getRole() == Role.VIEWER) {
            throw new CustomException("Access denied");
        }
    }

    // -------- API METHODS --------

    // Create new financial record (ADMIN only)
    @Operation(summary = "Create financial record (ADMIN only)")
    @PostMapping
    public ResponseEntity<FinancialRecord> createRecord(
            @Valid @RequestBody FinancialRecord record,
            @RequestParam Long userId) {

        User user = validateUser(userId);
        validateAdmin(user);

      // Id should not be provided in request body as it is auto-generated
        if (record.getId() != null) {
            throw new CustomException("Id should not be provided. It is auto-generated");
        }
        FinancialRecord saved = service.createRecord(record);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Get all records with pagination (accessible by all roles)
    @Operation(summary = "Get all financial records (ALL roles, paginated)")
    @GetMapping
    public Page<FinancialRecord> getAllRecords(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        validateUser(userId); // ensure user is active

        return service.getAllRecords(page, size);
    }

    // Full update of record (ADMIN only)
    @Operation(summary = "Update full financial record (ADMIN only)")
    @PutMapping("/{id}")
    public FinancialRecord updateRecord(@PathVariable Long id,
                                        @RequestBody FinancialRecord record,
                                        @RequestParam Long userId) {

        User user = validateUser(userId);
        validateAdmin(user);

        return service.updateRecord(id, record);
    }

    // Partial update of record (ADMIN only)
    @Operation(summary = "Update specific fields of a record (ADMIN only)")
    @PatchMapping("/{id}")
    public FinancialRecord patchRecord(@PathVariable Long id,
                                       @RequestBody FinancialRecord record,
                                       @RequestParam Long userId) {

        User user = validateUser(userId);
        validateAdmin(user);

        return service.patchRecord(id, record);
    }

    // Delete record (ADMIN only)
    @Operation(summary = "Delete a financial record (ADMIN only)")
    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable Long id,
                               @RequestParam Long userId) {

        User user = validateUser(userId);
        validateAdmin(user);

        service.deleteRecord(id);

        return "Record deleted successfully";
    }

    // Filter records with optional pagination (ALL roles)
    @Operation(summary = "Filter records (optional pagination)")
    @GetMapping("/filter")
    public Object filterRecords(
            @RequestParam(required = false) RecordType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDate date,
            @RequestParam Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        validateUser(userId);

        // If pagination parameters provided → return paginated result
        if (page != null && size != null) {
            return service.filterRecords(type, category, date, page, size);
        }

        // Else return full filtered list
        return service.filterRecordsWithoutPagination(type, category, date);
    }

    // Get overall summary (ADMIN + ANALYST only)
    @Operation(summary = "Get financial summary (ADMIN + ANALYST)")
    @GetMapping("/summary")
    public Map<String, Double> getSummary(@RequestParam Long userId) {

        User user = validateUser(userId);
        validateNotViewer(user);

        return service.getSummary();
    }

    // Get category-wise summary (ADMIN + ANALYST only)
    @Operation(summary = "Get category-wise summary (ADMIN + ANALYST)")
    @GetMapping("/summary/category")
    public Map<String, Double> getCategorySummary(@RequestParam Long userId) {

        User user = validateUser(userId);
        validateNotViewer(user);

        return service.getCategorySummary();
    }

    // Get recent records (ADMIN + ANALYST only)
    @Operation(summary = "Get recent records (ADMIN + ANALYST)")
    @GetMapping("/summary/recent")
    public List<FinancialRecord> getRecentRecords(@RequestParam Long userId) {

        User user = validateUser(userId);
        validateNotViewer(user);

        return service.getRecentRecords();
    }

    // Get monthly summary (ADMIN + ANALYST only)
    @Operation(summary = "Get monthly summary (ADMIN + ANALYST)")
    @GetMapping("/summary/monthly")
    public Map<Integer, Double> getMonthlySummary(@RequestParam Long userId) {

        User user = validateUser(userId);
        validateNotViewer(user);

        return service.getMonthlySummary();
    }
}