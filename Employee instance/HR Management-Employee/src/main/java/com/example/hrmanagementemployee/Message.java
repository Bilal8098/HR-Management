package com.example.hrmanagementemployee;

import javafx.beans.property.*;

public class Message {
    private final IntegerProperty messageId;
    private final StringProperty messageText;
    private final StringProperty createdAt;

    public Message(int messageId, String messageText, String createdAt) {
        this.messageId = new SimpleIntegerProperty(messageId);
        this.messageText = new SimpleStringProperty(messageText);
        this.createdAt = new SimpleStringProperty(createdAt);
    }

    public IntegerProperty messageIdProperty() {
        return messageId;
    }

    public StringProperty messageTextProperty() {
        return messageText;
    }

    public StringProperty createdAtProperty() {
        return createdAt;
    }
}
