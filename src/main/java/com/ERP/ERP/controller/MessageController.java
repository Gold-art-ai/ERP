package com.ERP.ERP.controller;

import com.ERP.ERP.model.Message;
import com.ERP.ERP.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Message>> getMessagesByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(messageService.getMessagesByEmployeeId(employeeId));
    }
}
