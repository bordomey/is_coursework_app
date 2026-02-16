package com.game.track.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UpdateSprintRequest {
    @Size(min = 1, max = 100)
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    // Constructors
    public UpdateSprintRequest() {}

    public UpdateSprintRequest(String name, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
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