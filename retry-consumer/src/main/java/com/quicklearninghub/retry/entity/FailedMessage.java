package com.quicklearninghub.retry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FailedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String message;

    @Column
    private String topic;

    @Column
    private Long consumerOffset;

    @Column
    private String exception;

    public FailedMessage() {}

    public FailedMessage(String message) {
        this.message = message;
    }

    public FailedMessage(String message, String topic, Long consumerOffset, String exception) {
        this.message = message;
        this.exception = exception;
        this.consumerOffset = consumerOffset;
        this.topic = topic;
    }
}
