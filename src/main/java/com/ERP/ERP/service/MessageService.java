package com.ERP.ERP.service;

import com.ERP.ERP.model.Message;
import com.ERP.ERP.model.Payslip;
import com.ERP.ERP.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Payslip payslip) {
        String content = String.format(
                "dear %s your salary of %s from %s %s FRW has been credited to your %s account successfully!",
                payslip.getEmployee().getFirstName(),
                payslip.getMonthYear().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                payslip.getEmployment().getInstitution(),
                payslip.getNetSalary().toString(),
                payslip.getEmployment().getEmployeeId()
        );

        Message message = new Message();
        message.setEmployee(payslip.getEmployee());
        message.setContent(content);
        message.setMonthYear(payslip.getMonthYear());
        message.setSentAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getMessagesByEmployeeId(Long employeeId) {
        return messageRepository.findByEmployee_Id(employeeId);
    }
}
