package com.game.track.controller;

import com.game.track.dto.CreateSprintRequest;
import com.game.track.dto.SprintDto;
import com.game.track.dto.UpdateSprintRequest;
import com.game.track.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/sprints")
public class SprintController {

    @Autowired
    private SprintService sprintService;

    @PostMapping
    public ResponseEntity<SprintDto> createSprint(@PathVariable Integer projectId, @Valid @RequestBody CreateSprintRequest request) {
        SprintDto sprint = sprintService.createSprint(projectId, request);
        return new ResponseEntity<>(sprint, HttpStatus.CREATED);
    }

    @GetMapping("/{sprintId}")
    public ResponseEntity<SprintDto> getSprint(@PathVariable Integer sprintId) {
        SprintDto sprint = sprintService.getSprintById(sprintId);
        return new ResponseEntity<>(sprint, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SprintDto>> getSprints(@PathVariable Integer projectId) {
        List<SprintDto> sprints = sprintService.getSprintsByProjectId(projectId);
        return new ResponseEntity<>(sprints, HttpStatus.OK);
    }

    @PutMapping("/{sprintId}")
    public ResponseEntity<SprintDto> updateSprint(@PathVariable Integer sprintId, @Valid @RequestBody UpdateSprintRequest request) {
        SprintDto updatedSprint = sprintService.updateSprint(sprintId, request);
        return new ResponseEntity<>(updatedSprint, HttpStatus.OK);
    }

    @DeleteMapping("/{sprintId}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Integer sprintId) {
        sprintService.deleteSprint(sprintId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}