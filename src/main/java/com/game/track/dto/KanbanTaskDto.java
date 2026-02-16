package com.game.track.dto;

public class KanbanTaskDto {
    private Integer taskId;
    private String title;
    private String status;
    private String priority;

    // Constructors
    public KanbanTaskDto() {}

    public KanbanTaskDto(Integer taskId, String title, String status, String priority) {
        this.taskId = taskId;
        this.title = title;
        this.status = status;
        this.priority = priority;
    }

    // Getters and Setters
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}