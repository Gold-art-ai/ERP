package com.ERP.ERP.controller;

import com.ERP.ERP.dto.EmploymentRequest;
import com.ERP.ERP.model.Employment;
import com.ERP.ERP.service.EmploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employments")
@CrossOrigin(origins = "*")
public class EmploymentController {

    @Autowired
    private EmploymentService employmentService;

    @PostMapping
    public ResponseEntity<Employment> createEmployment(@RequestBody EmploymentRequest request) {
        return ResponseEntity.ok(employmentService.createEmployment(request));
    }

    @GetMapping
    public ResponseEntity<List<Employment>> getAllEmployments() {
        return ResponseEntity.ok(employmentService.getAllEmployments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employment> getEmploymentById(@PathVariable Long id) {
        return employmentService.getEmploymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Employment>> getActiveEmployments() {
        return ResponseEntity.ok(employmentService.getActiveEmployments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employment> updateEmployment(@PathVariable Long id, @RequestBody EmploymentRequest request) {
        return ResponseEntity.ok(employmentService.updateEmployment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployment(@PathVariable Long id) {
        employmentService.deleteEmployment(id);
        return ResponseEntity.noContent().build();
    }
}
