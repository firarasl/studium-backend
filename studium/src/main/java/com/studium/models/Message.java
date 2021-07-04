package com.studium.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(	name = "message")

public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

private String text;

    @CreationTimestamp
    private Timestamp created;

    private Boolean isRead=false;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name="sender")
    private User sender;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name="receiver")
    private User receiver;

    public Message() {
    }

    public Message(Long id, String text, Timestamp created, boolean isRead) {
        this.id = id;
        this.text = text;
        this.created = created;
        this.isRead = isRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
