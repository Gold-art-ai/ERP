package com.ERP.ERP.service;

import com.ERP.ERP.model.Employee;
import com.ERP.ERP.model.Employment;
import com.ERP.ERP.model.Payslip;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generatePayslipPdf(Payslip payslip) {
        Employee employee = payslip.getEmployee();
        Employment employment = payslip.getEmployment();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Add title
            com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, java.awt.Color.BLUE);
            Paragraph title = new Paragraph("RWANDA GOVERNMENT ERP SYSTEM - PAYSLIP", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Add month and year
            com.lowagie.text.Font monthYearFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, java.awt.Color.BLACK);
            String monthYear = payslip.getMonthYear().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
            Paragraph monthYearParagraph = new Paragraph("Pay Period: " + monthYear, monthYearFont);
            monthYearParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(monthYearParagraph);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // Employee info table
            PdfPTable employeeTable = new PdfPTable(2);
            employeeTable.setWidthPercentage(100);
            employeeTable.setSpacingBefore(10f);
            employeeTable.setSpacingAfter(10f);

            addCell(employeeTable, "Employee ID:", true);
            addCell(employeeTable, employment.getEmployeeId(), false);

            addCell(employeeTable, "Employee Name:", true);
            addCell(employeeTable, employee.getFirstName() + " " + employee.getLastName(), false);

            addCell(employeeTable, "Department:", true);
            addCell(employeeTable, employment.getDepartment(), false);

            addCell(employeeTable, "Position:", true);
            addCell(employeeTable, employment.getPosition(), false);

            addCell(employeeTable, "Institution:", true);
            addCell(employeeTable, employment.getInstitution(), false);

            addCell(employeeTable, "Status:", true);
            addCell(employeeTable, payslip.getStatus().name(), false);

            document.add(employeeTable);
            document.add(Chunk.NEWLINE);

            // Salary details table
            PdfPTable salaryTable = new PdfPTable(2);
            salaryTable.setWidthPercentage(100);
            salaryTable.setSpacingBefore(10f);

            // Header
            com.lowagie.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, java.awt.Color.WHITE);
            PdfPCell header1 = new PdfPCell(new Phrase("DESCRIPTION", headerFont));
            header1.setBackgroundColor(java.awt.Color.GRAY);
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            salaryTable.addCell(header1);

            PdfPCell header2 = new PdfPCell(new Phrase("AMOUNT (FRW)", headerFont));
            header2.setBackgroundColor(java.awt.Color.GRAY);
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);
            salaryTable.addCell(header2);

            // Salary details
            addCell(salaryTable, "Base Salary:", true);
            addCell(salaryTable, payslip.getBaseSalary().toString(), false);

            addCell(salaryTable, "House Allowance:", true);
            addCell(salaryTable, payslip.getHouseAllowance().toString(), false);

            addCell(salaryTable, "Transport Allowance:", true);
            addCell(salaryTable, payslip.getTransportAllowance().toString(), false);

            // Gross salary row
            com.lowagie.text.Font grossFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, java.awt.Color.BLACK);
            PdfPCell grossLabel = new PdfPCell(new Phrase("GROSS SALARY:", grossFont));
            grossLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            grossLabel.setPadding(5);
            salaryTable.addCell(grossLabel);

            PdfPCell grossValue = new PdfPCell(new Phrase(payslip.getGrossSalary().toString(), grossFont));
            grossValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            grossValue.setPadding(5);
            salaryTable.addCell(grossValue);

            // Deductions
            addCell(salaryTable, "Employee Tax:", true);
            addCell(salaryTable, payslip.getEmployeeTax().toString(), false);

            addCell(salaryTable, "Pension:", true);
            addCell(salaryTable, payslip.getPansion().toString(), false);

            addCell(salaryTable, "Medical Insurance:", true);
            addCell(salaryTable, payslip.getMedicalInsurance().toString(), false);

            addCell(salaryTable, "Other Deductions:", true);
            addCell(salaryTable, payslip.getOthers().toString(), false);

            // Total deductions row
            PdfPCell totalDeductionsLabel = new PdfPCell(new Phrase("TOTAL DEDUCTIONS:", grossFont));
            totalDeductionsLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalDeductionsLabel.setPadding(5);
            salaryTable.addCell(totalDeductionsLabel);

            PdfPCell totalDeductionsValue = new PdfPCell(new Phrase(payslip.getTotalDeductions().toString(), grossFont));
            totalDeductionsValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalDeductionsValue.setPadding(5);
            salaryTable.addCell(totalDeductionsValue);

            // Net salary row
            com.lowagie.text.Font netFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, java.awt.Color.BLUE);
            PdfPCell netLabel = new PdfPCell(new Phrase("NET SALARY:", netFont));
            netLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            netLabel.setPadding(5);
            salaryTable.addCell(netLabel);

            PdfPCell netValue = new PdfPCell(new Phrase(payslip.getNetSalary().toString(), netFont));
            netValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            netValue.setPadding(5);
            salaryTable.addCell(netValue);

            document.add(salaryTable);

            // Footer
            document.add(Chunk.NEWLINE);
            com.lowagie.text.Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, java.awt.Color.GRAY);
            Paragraph footer = new Paragraph("Generated by Rwanda Government ERP System - " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF payslip: " + e.getMessage(), e);
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    private void addCell(PdfPTable table, String text, boolean isLabel) {
        com.lowagie.text.Font font;
        if (isLabel) {
            font = FontFactory.getFont(FontFactory.HELVETICA, 11, java.awt.Color.BLACK);
        } else {
            font = FontFactory.getFont(FontFactory.HELVETICA, 11, java.awt.Color.BLACK);
        }

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);

        if (isLabel) {
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        } else {
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        }

        table.addCell(cell);
    }
}
