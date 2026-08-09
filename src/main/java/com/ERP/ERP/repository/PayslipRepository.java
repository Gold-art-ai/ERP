package com.ERP.ERP.repository;

import com.ERP.ERP.model.Employee;
import com.ERP.ERP.model.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {
    List<Payslip> findByMonthYear(YearMonth monthYear);
    Optional<Payslip> findByEmployeeAndMonthYear(Employee employee, YearMonth monthYear);
    List<Payslip> findByEmployeeId(Long employeeId);
}
