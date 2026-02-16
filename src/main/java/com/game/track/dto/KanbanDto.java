package com.game.track.dto;

import java.util.List;

public class KanbanDto {
    private List<KanbanTaskDto> tasks;

    // Constructors
    public KanbanDto() {}

    public KanbanDto(List<KanbanTaskDto> tasks) {
        this.tasks = tasks;
    }

    // Getters and Setters
    public List<KanbanTaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<KanbanTaskDto> tasks) {
        this.tasks = tasks;
    }
}