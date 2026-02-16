package com.game.track.dto;

import jakarta.validation.constraints.Size;

public class UpdateProjectRequest {
    @Size(min = 1, max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    private Boolean isArchived;

    // Constructors
    public UpdateProjectRequest() {}

    public UpdateProjectRequest(String name, String description, Boolean isArchived) {
        this.name = name;
        this.description = description;
        this.isArchived = isArchived;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }
}