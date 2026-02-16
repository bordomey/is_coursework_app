package com.game.track.dto;

import jakarta.validation.constraints.NotNull;

public class AssignRoleRequest {
    @NotNull
    private Integer roleId;

    // Constructors
    public AssignRoleRequest() {}

    public AssignRoleRequest(Integer roleId) {
        this.roleId = roleId;
    }

    // Getters and Setters
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}