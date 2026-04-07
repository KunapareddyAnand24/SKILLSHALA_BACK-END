package com.placement.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String senderName;
    private String receiverName;
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;

    public MessageDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
