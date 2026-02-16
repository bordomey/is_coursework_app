package com.game.track.service;

import com.game.track.dto.CreateProjectRequest;
import com.game.track.dto.ProjectDto;
import com.game.track.dto.UpdateProjectRequest;
import com.game.track.entity.Project;
import com.game.track.exception.ResourceNotFoundException;
import com.game.track.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectDto createProject(CreateProjectRequest request) {
        Project project = new Project(request.getName(), request.getDescription());
        Project savedProject = projectRepository.save(project);
        return convertToDto(savedProject);
    }

    public ProjectDto getProjectById(Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        return convertToDto(project);
    }

    public List<ProjectDto> getAllProjects() {
        List<Project> projects = projectRepository.findByIsArchivedFalse();
        return projects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProjectDto updateProject(Integer projectId, UpdateProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getIsArchived() != null) {
            project.setIsArchived(request.getIsArchived());
        }

        Project updatedProject = projectRepository.save(project);
        return convertToDto(updatedProject);
    }

    public void archiveProject(Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        
        project.setIsArchived(true);
        projectRepository.save(project);
    }

    private ProjectDto convertToDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getIsArchived(),
                project.getCreatedAt()
        );
    }
}