package com.game.track.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTaskRequest {
    @NotNull
    private Integer projectId;

    private Integer sprintId;

    @NotBlank
    @Size(min = 1, max = 200)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    private Integer priorityId;

    // Constructors
    public CreateTaskRequest() {}

    public CreateTaskRequest(Integer projectId, Integer sprintId, String title, String description, Integer priorityId) {
        this.projectId = projectId;
        this.sprintId = sprintId;
        this.title = title;
        this.description = description;
        this.priorityId = priorityId;
    }

    // Getters and Setters
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getSprintId() {
        return sprintId;
    }

    public void setSprintId(Integer sprintId) {
        this.sprintId = sprintId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }
}