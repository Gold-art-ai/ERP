package com.ERP.ERP.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payslip")
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "employment_id", nullable = false)
    @JsonIgnore
    private Employment employment;

    @Column(nullable = false)
    private BigDecimal baseSalary;

    private BigDecimal houseAllowance;
    private BigDecimal transportAllowance;
    private BigDecimal grossSalary;

    private BigDecimal employeeTax;
    private BigDecimal pansion;
    private BigDecimal medicalInsurance;
    private BigDecimal others;
    private BigDecimal totalDeductions;

    private BigDecimal netSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayslipStatus status;

    @Column(nullable = false)
    private YearMonth monthYear;

    @com.fasterxml.jackson.annotation.JsonGetter("employeeId")
    public Long getEmployeeId() {
        return employee != null ? employee.getId() : null;
    }

    @com.fasterxml.jackson.annotation.JsonGetter("employeeName")
    public String getEmployeeName() {
        return employee != null ? employee.getFirstName() + " " + employee.getLastName() : null;
    }

    @com.fasterxml.jackson.annotation.JsonGetter("employmentId")
    public Long getEmploymentId() {
        return employment != null ? employment.getId() : null;
    }

    @com.fasterxml.jackson.annotation.JsonGetter("employeeIdentifier")
    public String getEmployeeIdentifier() {
        return employment != null ? employment.getEmployeeId() : null;
    }

    @com.fasterxml.jackson.annotation.JsonGetter("institution")
    public String getInstitution() {
        return employment != null ? employment.getInstitution() : null;
    }
}
