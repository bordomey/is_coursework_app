package com.game.track.dto;

import java.time.LocalDateTime;

public class ProjectDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean isArchived;
    private LocalDateTime createdAt;

    // Constructors
    public ProjectDto() {}

    public ProjectDto(Integer id, String name, String description, Boolean isArchived, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isArchived = isArchived;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}