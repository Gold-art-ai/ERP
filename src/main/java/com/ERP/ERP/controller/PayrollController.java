package com.ERP.ERP.controller;

import com.ERP.ERP.dto.PayrollRequest;
import com.ERP.ERP.model.Payslip;
import com.ERP.ERP.service.PayrollService;
import com.ERP.ERP.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@CrossOrigin(origins = "*")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private PdfService pdfService;

    @PostMapping("/generate")
    public ResponseEntity<List<Payslip>> generatePayroll(@RequestBody PayrollRequest request) {
        return ResponseEntity.ok(payrollService.generatePayroll(request));
    }

    @PostMapping("/approve")
    public ResponseEntity<List<Payslip>> approvePayroll(@RequestBody PayrollRequest request) {
        return ResponseEntity.ok(payrollService.approvePayroll(request));
    }

    @GetMapping("/payslips")
    public ResponseEntity<List<Payslip>> getAllPayslips() {
        return ResponseEntity.ok(payrollService.getAllPayslips());
    }

    @GetMapping("/payslips/month")
    public ResponseEntity<List<Payslip>> getPayslipsByMonthYear(@RequestBody PayrollRequest request) {
        return ResponseEntity.ok(payrollService.getPayslipsByMonthYear(request));
    }

    @GetMapping("/payslips/employee/{employeeId}")
    public ResponseEntity<List<Payslip>> getPayslipsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payrollService.getPayslipsByEmployeeId(employeeId));
    }

    @GetMapping("/payslips/{payslipId}/download")
    public ResponseEntity<byte[]> downloadPayslip(@PathVariable Long payslipId) {
        Payslip payslip = payrollService.getPayslipById(payslipId);

        byte[] pdfBytes = pdfService.generatePayslipPdf(payslip);

        String filename = "Payslip_" + payslip.getEmployee().getFirstName() + "_" + payslip.getMonthYear().toString() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
