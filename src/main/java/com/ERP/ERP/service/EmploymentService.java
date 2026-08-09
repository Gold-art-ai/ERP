package com.ERP.ERP.service;

import com.ERP.ERP.dto.EmploymentRequest;
import com.ERP.ERP.model.Employee;
import com.ERP.ERP.model.Employment;
import com.ERP.ERP.model.EmploymentStatus;
import com.ERP.ERP.repository.EmployeeRepository;
import com.ERP.ERP.repository.EmploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmploymentService {

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employment createEmployment(EmploymentRequest request) {
        Optional<Employee> employeeOpt = employeeRepository.findById(request.getEmployeeIdFromEmployee());
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Cannot create employment: Employee with id " + request.getEmployeeIdFromEmployee() + " not found");
        }

        // Check if employeeId is already taken
        if (employmentRepository.findByEmployeeId(request.getEmployeeId()).isPresent()) {
            throw new RuntimeException("Cannot create employment: Employee ID '" + request.getEmployeeId() + "' already exists");
        }

        Employment employment = new Employment();
        employment.setEmployeeId(request.getEmployeeId());
        employment.setEmployee(employeeOpt.get());
        employment.setDepartment(request.getDepartment());
        employment.setPosition(request.getPosition());
        employment.setBaseSalary(request.getBaseSalary());
        employment.setStatus(EmploymentStatus.valueOf(request.getStatus().toUpperCase()));
        employment.setJoiningDate(request.getJoiningDate());
        employment.setInstitution(request.getInstitution());
        return employmentRepository.save(employment);
    }

    public List<Employment> getAllEmployments() {
        return employmentRepository.findAll();
    }

    public Optional<Employment> getEmploymentById(Long id) {
        return employmentRepository.findById(id);
    }

    public List<Employment> getActiveEmployments() {
        return employmentRepository.findByStatus(EmploymentStatus.ACTIVE);
    }

    public Employment updateEmployment(Long id, EmploymentRequest request) {
        return employmentRepository.findById(id).map(employment -> {
            employment.setEmployeeId(request.getEmployeeId());
            employment.setDepartment(request.getDepartment());
            employment.setPosition(request.getPosition());
            employment.setBaseSalary(request.getBaseSalary());
            employment.setStatus(EmploymentStatus.valueOf(request.getStatus().toUpperCase()));
            employment.setJoiningDate(request.getJoiningDate());
            employment.setInstitution(request.getInstitution());
            return employmentRepository.save(employment);
        }).orElseThrow(() -> new RuntimeException("Cannot update: Employment not found with id: " + id));
    }

    public void deleteEmployment(Long id) {
        if (!employmentRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Employment not found with id: " + id);
        }
        employmentRepository.deleteById(id);
    }
}
