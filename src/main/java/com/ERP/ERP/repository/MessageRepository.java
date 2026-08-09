package com.ERP.ERP.repository;

import com.ERP.ERP.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEmployee_Id(Long employeeId);
}
