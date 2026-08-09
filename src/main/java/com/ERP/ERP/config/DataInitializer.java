package com.ERP.ERP.config;

import com.ERP.ERP.model.Deduction;
import com.ERP.ERP.model.Employee;
import com.ERP.ERP.model.Employment;
import com.ERP.ERP.model.EmploymentStatus;
import com.ERP.ERP.repository.DeductionRepository;
import com.ERP.ERP.repository.EmployeeRepository;
import com.ERP.ERP.repository.EmploymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private DeductionRepository deductionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentRepository employmentRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting data initialization...");

        try {
            // Initialize Deductions
            if (deductionRepository.count() == 0) {
                logger.info("Initializing default deductions...");
                Deduction d1 = new Deduction();
                d1.setName("EmployeeTax");
                d1.setPercentage(30.0);
                deductionRepository.save(d1);

                Deduction d2 = new Deduction();
                d2.setName("Pansion");
                d2.setPercentage(6.0);
                deductionRepository.save(d2);

                Deduction d3 = new Deduction();
                d3.setName("MedicalInsurance");
                d3.setPercentage(5.0);
                deductionRepository.save(d3);

                Deduction d4 = new Deduction();
                d4.setName("Others");
                d4.setPercentage(5.0);
                deductionRepository.save(d4);

                Deduction d5 = new Deduction();
                d5.setName("House");
                d5.setPercentage(14.0);
                deductionRepository.save(d5);

                Deduction d6 = new Deduction();
                d6.setName("Transport");
                d6.setPercentage(14.0);
                deductionRepository.save(d6);
                logger.info("Successfully initialized 6 default deductions!");
            } else {
                logger.info("Deductions already initialized. Skipping...");
            }

            // Initialize Sample Employees
            if (employeeRepository.count() == 0) {
                logger.info("Initializing sample employees...");
                Employee employee1 = new Employee();
                employee1.setFirstName("Mugabo");
                employee1.setLastName("Javis");
                employee1.setEmail("mugabo@example.com");
                employee1.setDistrict("Kigali");
                employee1.setMobile("0781234567");
                employee1.setDateOfBirth(LocalDate.of(1990, 1, 1));

                Employee employee2 = new Employee();
                employee2.setFirstName("Michou");
                employee2.setLastName("Michell");
                employee2.setEmail("michou@example.com");
                employee2.setDistrict("Huye");
                employee2.setMobile("0789876543");
                employee2.setDateOfBirth(LocalDate.of(1995, 5, 15));

                employee1 = employeeRepository.save(employee1);
                employee2 = employeeRepository.save(employee2);
                logger.info("Successfully saved 2 sample employees!");

                logger.info("Initializing sample employments...");
                Employment employment1 = new Employment();
                employment1.setEmployeeId("EMP001");
                employment1.setEmployee(employee1);
                employment1.setDepartment("HR");
                employment1.setPosition("Manager");
                employment1.setBaseSalary(70000.0);
                employment1.setStatus(EmploymentStatus.ACTIVE);
                employment1.setJoiningDate(LocalDate.of(2020, 1, 1));
                employment1.setInstitution("RCA");

                Employment employment2 = new Employment();
                employment2.setEmployeeId("EMP002");
                employment2.setEmployee(employee2);
                employment2.setDepartment("Finance");
                employment2.setPosition("Accountant");
                employment2.setBaseSalary(35000.0);
                employment2.setStatus(EmploymentStatus.ACTIVE);
                employment2.setJoiningDate(LocalDate.of(2021, 6, 1));
                employment2.setInstitution("RCA");

                employmentRepository.save(employment1);
                employmentRepository.save(employment2);
                logger.info("Successfully saved 2 sample employments!");
            } else {
                logger.info("Sample employees already initialized. Skipping...");
            }

            logger.info("Data initialization completed successfully!");
        } catch (Exception e) {
            logger.error("Error during data initialization: {}", e.getMessage(), e);
            throw e;
        }
    }
}
