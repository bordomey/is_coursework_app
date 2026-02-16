package com.game.track.dto;

import jakarta.validation.constraints.NotNull;

public class AddMemberRequest {
    @NotNull
    private Integer userId;

    // Constructors
    public AddMemberRequest() {}

    public AddMemberRequest(Integer userId) {
        this.userId = userId;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}