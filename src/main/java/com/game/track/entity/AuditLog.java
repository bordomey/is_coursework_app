package com.game.track.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "old_status_id")
    private Integer oldStatusId;

    @Column(name = "new_status_id")
    private Integer newStatusId;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    // Constructors
    public AuditLog() {}

    public AuditLog(Task task, Integer oldStatusId, Integer newStatusId) {
        this.task = task;
        this.oldStatusId = oldStatusId;
        this.newStatusId = newStatusId;
        this.changedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getOldStatusId() {
        return oldStatusId;
    }

    public void setOldStatusId(Integer oldStatusId) {
        this.oldStatusId = oldStatusId;
    }

    public Integer getNewStatusId() {
        return newStatusId;
    }

    public void setNewStatusId(Integer newStatusId) {
        this.newStatusId = newStatusId;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}