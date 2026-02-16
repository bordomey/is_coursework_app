package com.game.track.dto;

import java.time.LocalDate;

public class SprintDto {
    private Integer id;
    private Integer projectId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    // Constructors
    public SprintDto() {}

    public SprintDto(Integer id, Integer projectId, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}