package com.game.track.dto;

import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
    @Size(min = 2, max = 50)
    private String fullName;

    // Constructors
    public UpdateUserRequest() {}

    public UpdateUserRequest(String fullName) {
        this.fullName = fullName;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}