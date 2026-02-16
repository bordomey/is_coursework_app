package com.game.track.dto;

import jakarta.validation.constraints.NotNull;

public class ChangeStatusRequest {
    @NotNull
    private Integer statusId;

    // Constructors
    public ChangeStatusRequest() {}

    public ChangeStatusRequest(Integer statusId) {
        this.statusId = statusId;
    }

    // Getters and Setters
    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}