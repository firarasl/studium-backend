package com.bezkoder.springjwt.payload.request;

public class MessageRequest {

    private String text;
    private String receiverUsername;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }
}
