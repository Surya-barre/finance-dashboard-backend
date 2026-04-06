package com.finance.finance_dashboard_backend.entity;

import com.finance.finance_dashboard_backend.enums.RecordType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//Entity class representing a financial records
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)    
    private RecordType type; // INCOME or EXPENSE

    @NotBlank
    private String category;

    @PastOrPresent
    @NotNull
    private LocalDate date;

    private String note;

}