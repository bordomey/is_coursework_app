package com.game.track.service;

import com.game.track.dto.CreateSprintRequest;
import com.game.track.dto.SprintDto;
import com.game.track.dto.UpdateSprintRequest;
import com.game.track.entity.Project;
import com.game.track.entity.Sprint;
import com.game.track.exception.ResourceNotFoundException;
import com.game.track.repository.ProjectRepository;
import com.game.track.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public SprintDto createSprint(Integer projectId, CreateSprintRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Sprint sprint = new Sprint(project, request.getName(), request.getStartDate(), request.getEndDate());
        Sprint savedSprint = sprintRepository.save(sprint);
        return convertToDto(savedSprint);
    }

    public SprintDto getSprintById(Integer sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id: " + sprintId));
        return convertToDto(sprint);
    }

    public List<SprintDto> getSprintsByProjectId(Integer projectId) {
        List<Sprint> sprints = sprintRepository.findByProjectId(projectId);
        return sprints.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SprintDto updateSprint(Integer sprintId, UpdateSprintRequest request) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id: " + sprintId));

        if (request.getName() != null) {
            sprint.setName(request.getName());
        }
        if (request.getStartDate() != null) {
            sprint.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            sprint.setEndDate(request.getEndDate());
        }

        Sprint updatedSprint = sprintRepository.save(sprint);
        return convertToDto(updatedSprint);
    }

    public void deleteSprint(Integer sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id: " + sprintId));
        sprintRepository.delete(sprint);
    }

    private SprintDto convertToDto(Sprint sprint) {
        return new SprintDto(
                sprint.getId(),
                sprint.getProject().getId(),
                sprint.getName(),
                sprint.getStartDate(),
                sprint.getEndDate()
        );
    }
}