package com.ERP.ERP.service;

import com.ERP.ERP.dto.EmployeeRequest;
import com.ERP.ERP.model.Employee;
import com.ERP.ERP.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee createEmployee(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDistrict(request.getDistrict());
        employee.setMobile(request.getMobile());
        employee.setDateOfBirth(request.getDateOfBirth());
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(Long id, EmployeeRequest request) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setFirstName(request.getFirstName());
            employee.setLastName(request.getLastName());
            employee.setEmail(request.getEmail());
            employee.setDistrict(request.getDistrict());
            employee.setMobile(request.getMobile());
            employee.setDateOfBirth(request.getDateOfBirth());
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
}
