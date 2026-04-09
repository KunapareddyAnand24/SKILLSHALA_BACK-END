package com.placement.controller;

import com.placement.dto.MessageDTO;
import com.placement.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = {"http://localhost:5173", "https://skill-shala.netlify.app"})
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'EMPLOYER', 'OFFICER', 'ADMIN')")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(messageService.sendMessage(messageDTO));
    }

    @GetMapping("/conversation/{u1}/{u2}")
    @PreAuthorize("hasAnyRole('STUDENT', 'EMPLOYER', 'OFFICER', 'ADMIN')")
    public List<MessageDTO> getConversation(@PathVariable Long u1, @PathVariable Long u2) {
        return messageService.getConversation(u1, u2);
    }

    @GetMapping("/inbox/{userId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'EMPLOYER', 'OFFICER', 'ADMIN')")
    public List<MessageDTO> getInbox(@PathVariable Long userId) {
        return messageService.getInbox(userId);
    }
}
