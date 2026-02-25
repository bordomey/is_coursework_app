package com.game.track.controller;

import com.game.track.dto.AssignTaskRequest;
import com.game.track.dto.ChangeStatusRequest;
import com.game.track.dto.CreateTaskRequest;
import com.game.track.dto.TaskDto;
import com.game.track.dto.UpdateTaskRequest;
import com.game.track.repository.TaskPriorityRepository;
import com.game.track.entity.TaskPriority;
import com.game.track.service.TaskService;
import com.game.track.service.YandexTrackerService;
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
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    @Autowired
    private YandexTrackerService yandexTrackerService;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskDto createdTask = taskService.createTask(request);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/priorities")
    public ResponseEntity<List<TaskPriority>> getPriorities() {
        List<TaskPriority> priorities = taskPriorityRepository.findAll();
        return new ResponseEntity<>(priorities, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks() {
        List<TaskDto> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Integer taskId) {
        TaskDto task = taskService.getTaskById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Integer taskId, @Valid @RequestBody UpdateTaskRequest request) {
        TaskDto updatedTask = taskService.updateTask(taskId, request);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{taskId}/assignee")
    public ResponseEntity<Void> assignTask(@PathVariable Integer taskId, @Valid @RequestBody AssignTaskRequest request) {
        taskService.assignTask(taskId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}/assignee")
    public ResponseEntity<Void> removeAssignee(@PathVariable Integer taskId) {
        taskService.removeAssignee(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<Void> changeTaskStatus(@PathVariable Integer taskId, @Valid @RequestBody ChangeStatusRequest request) {
        taskService.changeTaskStatus(taskId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{taskId}/sync")
    public ResponseEntity<String> syncTaskToYandexTracker(@PathVariable Integer taskId) {
        try {
            boolean success = yandexTrackerService.syncTaskToYandexTracker(taskService.getTaskEntityById(taskId));
            if (success) {
                return new ResponseEntity<>("Task synced to Yandex Tracker successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Task sync to Yandex Tracker failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error syncing task to Yandex Tracker: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}