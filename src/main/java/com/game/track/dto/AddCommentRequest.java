package com.game.track.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddCommentRequest {
    @NotNull
    private Integer userId;

    @Size(min = 1, max = 1000)
    private String content;

    // Constructors
    public AddCommentRequest() {}

    public AddCommentRequest(Integer userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}