package com.example.hrmanagementmanager;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private String messageText;
    private Timestamp createdAt;

    public Message(int messageId, String messageText, Timestamp createdAt) {
        this.messageId = messageId;
        this.messageText = messageText;
        this.createdAt = createdAt;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
