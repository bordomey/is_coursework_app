package com.game.track.dto;

import jakarta.validation.constraints.NotNull;

public class AssignTaskRequest {
    @NotNull
    private Integer userId;

    // Constructors
    public AssignTaskRequest() {}

    public AssignTaskRequest(Integer userId) {
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