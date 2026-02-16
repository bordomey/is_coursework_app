package com.game.track.repository;

import com.game.track.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByProjectId(Integer projectId);

    List<Task> findBySprintId(Integer sprintId);

    @Modifying
    @Query("UPDATE Task t SET t.status.id = :statusId WHERE t.id = :taskId")
    void updateTaskStatus(@Param("taskId") Integer taskId, @Param("statusId") Integer statusId);

    @Query(value = "SELECT * FROM tasks t WHERE t.id IN " +
           "(SELECT ta.task_id FROM task_assignees ta WHERE ta.user_id = :userId)", 
           nativeQuery = true)
    List<Task> findByAssigneeId(@Param("userId") Integer userId);
}