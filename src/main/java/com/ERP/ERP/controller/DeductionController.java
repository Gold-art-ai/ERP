package com.ERP.ERP.controller;

import com.ERP.ERP.model.Deduction;
import com.ERP.ERP.service.DeductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deductions")
@CrossOrigin(origins = "*")
public class DeductionController {

    @Autowired
    private DeductionService deductionService;

    @PostMapping
    public ResponseEntity<Deduction> createDeduction(@RequestBody Deduction deduction) {
        return ResponseEntity.ok(deductionService.createDeduction(deduction));
    }

    @GetMapping
    public ResponseEntity<List<Deduction>> getAllDeductions() {
        return ResponseEntity.ok(deductionService.getAllDeductions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deduction> getDeductionById(@PathVariable Long id) {
        return deductionService.getDeductionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deduction> updateDeduction(@PathVariable Long id, @RequestBody Deduction deduction) {
        return ResponseEntity.ok(deductionService.updateDeduction(id, deduction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeduction(@PathVariable Long id) {
        deductionService.deleteDeduction(id);
        return ResponseEntity.noContent().build();
    }
}
