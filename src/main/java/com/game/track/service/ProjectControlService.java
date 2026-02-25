package com.game.track.service;

import com.game.track.dto.KanbanDto;
import com.game.track.dto.KanbanTaskDto;
import com.game.track.repository.CustomProjectRepository;
import com.game.track.repository.CustomProjectRepository;
import com.game.track.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjectControlService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CustomProjectRepository customProjectRepository;

    public KanbanDto getKanbanBoard(Integer projectId) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        List<KanbanTaskDto> tasks = customProjectRepository.getKanbanBoard(projectId);
        return new KanbanDto(tasks);
    }
}