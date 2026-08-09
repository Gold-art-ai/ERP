package com.ERP.ERP.repository;

import com.ERP.ERP.model.Employment;
import com.ERP.ERP.model.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, Long> {
    Optional<Employment> findByEmployeeId(String employeeId);
    List<Employment> findByStatus(EmploymentStatus status);
    Optional<Employment> findByEmployeeId(Long employeeId);
}
