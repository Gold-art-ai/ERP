package com.ERP.ERP.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "system_messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private YearMonth monthYear;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @com.fasterxml.jackson.annotation.JsonGetter("employeeId")
    public Long getEmployeeId() {
        return employee != null ? employee.getId() : null;
    }

    @com.fasterxml.jackson.annotation.JsonGetter("employeeName")
    public String getEmployeeName() {
        return employee != null ? employee.getFirstName() + " " + employee.getLastName() : null;
    }
}
