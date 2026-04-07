package com.placement.service;

import com.placement.dto.MessageDTO;
import com.placement.model.Message;
import com.placement.repository.MessageRepository;
import com.placement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public MessageDTO sendMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setSenderId(messageDTO.getSenderId());
        message.setReceiverId(messageDTO.getReceiverId());
        message.setContent(messageDTO.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);

        Message saved = messageRepository.save(message);
        return convertToDTO(saved);
    }

    public List<MessageDTO> getConversation(Long u1, Long u2) {
        return messageRepository.findConversation(u1, u2).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MessageDTO> getInbox(Long userId) {
        return messageRepository.findByReceiverIdOrderBySentAtDesc(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MessageDTO convertToDTO(Message m) {
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());
        dto.setSenderId(m.getSenderId());
        dto.setReceiverId(m.getReceiverId());
        dto.setContent(m.getContent());
        dto.setSentAt(m.getSentAt());
        dto.setRead(m.isRead());

        // Attach names
        userRepository.findById(m.getSenderId()).ifPresent(u -> dto.setSenderName(u.getName()));
        userRepository.findById(m.getReceiverId()).ifPresent(u -> dto.setReceiverName(u.getName()));

        return dto;
    }
}
