package com.game.track.dto;

import jakarta.validation.constraints.Size;

public class UpdateTaskRequest {
    @Size(min = 1, max = 200)
    private String title;

    @Size(max = 1000)
    private String description;

    private Integer priorityId;

    private Integer sprintId;

    // Constructors
    public UpdateTaskRequest() {}

    public UpdateTaskRequest(String title, String description, Integer priorityId, Integer sprintId) {
        this.title = title;
        this.description = description;
        this.priorityId = priorityId;
        this.sprintId = sprintId;
    }

    // Getters and Setters
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

    public Integer getSprintId() {
        return sprintId;
    }

    public void setSprintId(Integer sprintId) {
        this.sprintId = sprintId;
    }
}