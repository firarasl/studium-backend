package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MessageRequest {
@NotNull
@NotEmpty
@Size(min = 3)
    private String text;
    @NotNull
    @NotEmpty
    @Size(min = 3)
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
