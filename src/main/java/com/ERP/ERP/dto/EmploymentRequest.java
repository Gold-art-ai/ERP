package com.ERP.ERP.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentRequest {
    private String employeeId;
    private Long employeeIdFromEmployee;
    private String department;
    private String position;
    private Double baseSalary;
    private String status;
    private LocalDate joiningDate;
    private String institution;
}
