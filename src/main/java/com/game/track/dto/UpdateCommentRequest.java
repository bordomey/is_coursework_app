package com.game.track.dto;

import jakarta.validation.constraints.Size;

public class UpdateCommentRequest {
    @Size(min = 1, max = 1000)
    private String content;

    // Constructors
    public UpdateCommentRequest() {}

    public UpdateCommentRequest(String content) {
        this.content = content;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}