package com.game.track.service;

import com.game.track.dto.AssignTaskRequest;
import com.game.track.dto.ChangeStatusRequest;
import com.game.track.dto.CreateTaskRequest;
import com.game.track.dto.TaskDto;
import com.game.track.dto.UpdateTaskRequest;
import com.game.track.entity.Project;
import com.game.track.entity.Sprint;
import com.game.track.entity.Task;
import com.game.track.entity.TaskPriority;
import com.game.track.entity.TaskStatus;
import com.game.track.entity.User;
import com.game.track.exception.ResourceNotFoundException;
import com.game.track.repository.ProjectRepository;
import com.game.track.repository.SprintRepository;
import com.game.track.repository.TaskPriorityRepository;
import com.game.track.repository.TaskRepository;
import com.game.track.repository.TaskStatusRepository;
import com.game.track.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    @Autowired
    private UserRepository userRepository;

    public TaskDto createTask(CreateTaskRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));

        Sprint sprint = null;
        if (request.getSprintId() != null) {
            sprint = sprintRepository.findById(request.getSprintId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id: " + request.getSprintId()));
        }

        TaskPriority priority = taskPriorityRepository.findById(request.getPriorityId())
                .orElseThrow(() -> new ResourceNotFoundException("Task priority not found with id: " + request.getPriorityId()));

        TaskStatus status = taskStatusRepository.findByName("To Do")
                .orElseThrow(() -> new ResourceNotFoundException("Task status 'To Do' not found"));

        Task task = new Task(project, sprint, request.getTitle(), request.getDescription(), status, priority);
        Task savedTask = taskRepository.save(task);
        return convertToDto(savedTask);
    }

    public TaskDto getTaskById(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        return convertToDto(task);
    }

    public Task getTaskEntityById(Integer taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
    }

    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByProjectId(Integer projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TaskDto updateTask(Integer taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getPriorityId() != null) {
            TaskPriority priority = taskPriorityRepository.findById(request.getPriorityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task priority not found with id: " + request.getPriorityId()));
            task.setPriority(priority);
        }
        if (request.getSprintId() != null) {
            Sprint sprint = sprintRepository.findById(request.getSprintId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id: " + request.getSprintId()));
            task.setSprint(sprint);
        }

        Task updatedTask = taskRepository.save(task);
        return convertToDto(updatedTask);
    }

    public void deleteTask(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        taskRepository.delete(task);
    }

    public void assignTask(Integer taskId, AssignTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

    }

    public void changeTaskStatus(Integer taskId, ChangeStatusRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        TaskStatus status = taskStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Task status not found with id: " + request.getStatusId()));

        task.setStatus(status);
        taskRepository.save(task);
    }

    public void removeAssignee(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

    }

    private TaskDto convertToDto(Task task) {
        String statusName = task.getStatus() != null ? task.getStatus().getName() : null;
        String priorityName = task.getPriority() != null ? task.getPriority().getName() : null;

        return new TaskDto(
                task.getId(),
                task.getProject().getId(),
                task.getSprint() != null ? task.getSprint().getId() : null,
                task.getTitle(),
                task.getDescription(),
                task.getStatus().getId(),
                statusName,
                priorityName,
                null, 
                null, 
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}