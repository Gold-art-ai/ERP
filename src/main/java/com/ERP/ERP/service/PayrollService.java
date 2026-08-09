package com.ERP.ERP.service;

import com.ERP.ERP.dto.PayrollRequest;
import com.ERP.ERP.model.*;
import com.ERP.ERP.repository.DeductionRepository;
import com.ERP.ERP.repository.EmploymentRepository;
import com.ERP.ERP.repository.PayslipRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayrollService {

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private DeductionRepository deductionRepository;

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private MessageService messageService;

    @Transactional
    public List<Payslip> generatePayroll(PayrollRequest request) {
        YearMonth monthYear = YearMonth.of(request.getYear(), request.getMonth());
        List<Employment> activeEmployments = employmentRepository.findByStatus(EmploymentStatus.ACTIVE);
        List<Payslip> payslips = new ArrayList<>();

        for (Employment employment : activeEmployments) {
            if (payslipRepository.findByEmployeeAndMonthYear(employment.getEmployee(), monthYear).isPresent()) {
                System.out.println("Skipping payroll for employee " + employment.getEmployee().getFirstName() + 
                    " - payslip already exists for " + monthYear);
                continue;
            }

            Payslip payslip = calculatePayslip(employment, monthYear);
            payslips.add(payslipRepository.save(payslip));
        }

        return payslips;
    }

    private Payslip calculatePayslip(Employment employment, YearMonth monthYear) {
        BigDecimal baseSalary = BigDecimal.valueOf(employment.getBaseSalary());

        Deduction houseDeduction = deductionRepository.findByName("House")
                .orElseThrow(() -> new RuntimeException("Missing required deduction: 'House'"));
        Deduction transportDeduction = deductionRepository.findByName("Transport")
                .orElseThrow(() -> new RuntimeException("Missing required deduction: 'Transport'"));
        Deduction employeeTaxDeduction = deductionRepository.findByName("EmployeeTax")
                .orElseThrow(() -> new RuntimeException("Missing required deduction: 'EmployeeTax'"));
        Deduction pansionDeduction = deductionRepository.findByName("Pansion")
                .orElseThrow(() -> new RuntimeException("Missing required deduction: 'Pansion'"));
        Deduction medicalInsuranceDeduction = deductionRepository.findByName("MedicalInsurance")
                .orElseThrow(() -> new RuntimeException("Missing required deduction: 'MedicalInsurance'"));
        Deduction othersDeduction = deductionRepository.findByName("Others")
                .orElseThrow(() -> new RuntimeException("Missing required deduction: 'Others'"));

        BigDecimal houseAllowance = baseSalary.multiply(BigDecimal.valueOf(houseDeduction.getPercentage())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal transportAllowance = baseSalary.multiply(BigDecimal.valueOf(transportDeduction.getPercentage())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal grossSalary = baseSalary.add(houseAllowance).add(transportAllowance);

        BigDecimal employeeTax = baseSalary.multiply(BigDecimal.valueOf(employeeTaxDeduction.getPercentage())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal pansion = baseSalary.multiply(BigDecimal.valueOf(pansionDeduction.getPercentage())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal medicalInsurance = baseSalary.multiply(BigDecimal.valueOf(medicalInsuranceDeduction.getPercentage())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal others = baseSalary.multiply(BigDecimal.valueOf(othersDeduction.getPercentage())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalDeductions = employeeTax.add(pansion).add(medicalInsurance).add(others);

        BigDecimal netSalary = baseSalary.subtract(totalDeductions);

        Payslip payslip = new Payslip();
        payslip.setEmployee(employment.getEmployee());
        payslip.setEmployment(employment);
        payslip.setBaseSalary(baseSalary);
        payslip.setHouseAllowance(houseAllowance);
        payslip.setTransportAllowance(transportAllowance);
        payslip.setGrossSalary(grossSalary);
        payslip.setEmployeeTax(employeeTax);
        payslip.setPansion(pansion);
        payslip.setMedicalInsurance(medicalInsurance);
        payslip.setOthers(others);
        payslip.setTotalDeductions(totalDeductions);
        payslip.setNetSalary(netSalary);
        payslip.setStatus(PayslipStatus.PENDING);
        payslip.setMonthYear(monthYear);

        return payslip;
    }

    @Transactional
    public List<Payslip> approvePayroll(PayrollRequest request) {
        YearMonth monthYear = YearMonth.of(request.getYear(), request.getMonth());
        List<Payslip> payslips = payslipRepository.findByMonthYear(monthYear);

        for (Payslip payslip : payslips) {
            if (payslip.getStatus() == PayslipStatus.PENDING) {
                payslip.setStatus(PayslipStatus.PAID);
                payslipRepository.save(payslip);

                messageService.createMessage(payslip);
            }
        }

        return payslips;
    }

    public List<Payslip> getAllPayslips() {
        return payslipRepository.findAll();
    }

    public List<Payslip> getPayslipsByMonthYear(PayrollRequest request) {
        YearMonth monthYear = YearMonth.of(request.getYear(), request.getMonth());
        return payslipRepository.findByMonthYear(monthYear);
    }

    public List<Payslip> getPayslipsByEmployeeId(Long employeeId) {
        return payslipRepository.findByEmployeeId(employeeId);
    }

    public Payslip getPayslipById(Long payslipId) {
        return payslipRepository.findById(payslipId)
                .orElseThrow(() -> new RuntimeException("Payslip not found with id: " + payslipId));
    }
}
