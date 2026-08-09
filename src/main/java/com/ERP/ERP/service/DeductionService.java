package com.ERP.ERP.service;

import com.ERP.ERP.model.Deduction;
import com.ERP.ERP.repository.DeductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeductionService {

    @Autowired
    private DeductionRepository deductionRepository;

    public Deduction createDeduction(Deduction deduction) {
        if (deductionRepository.findByName(deduction.getName()).isPresent()) {
            throw new RuntimeException("Cannot create deduction: Deduction with name '" + deduction.getName() + "' already exists");
        }
        return deductionRepository.save(deduction);
    }

    public List<Deduction> getAllDeductions() {
        return deductionRepository.findAll();
    }

    public Optional<Deduction> getDeductionById(Long id) {
        return deductionRepository.findById(id);
    }

    public Optional<Deduction> getDeductionByName(String name) {
        return deductionRepository.findByName(name);
    }

    public Deduction updateDeduction(Long id, Deduction deduction) {
        return deductionRepository.findById(id).map(d -> {
            d.setName(deduction.getName());
            d.setPercentage(deduction.getPercentage());
            return deductionRepository.save(d);
        }).orElseThrow(() -> new RuntimeException("Cannot update: Deduction not found with id: " + id));
    }

    public void deleteDeduction(Long id) {
        if (!deductionRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Deduction not found with id: " + id);
        }
        deductionRepository.deleteById(id);
    }
}
