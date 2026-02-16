package com.game.track.controller;

import com.game.track.dto.KanbanDto;
import com.game.track.service.ProjectControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects/{projectId}")
public class ProjectControlController {

    @Autowired
    private ProjectControlService projectControlService;

    @GetMapping("/kanban")
    public ResponseEntity<KanbanDto> getKanbanBoard(@PathVariable Integer projectId) {
        KanbanDto kanban = projectControlService.getKanbanBoard(projectId);
        return new ResponseEntity<>(kanban, HttpStatus.OK);
    }
}