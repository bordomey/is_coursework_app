package com.game.track.repository;

import com.game.track.entity.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Integer> {
    Optional<TaskPriority> findByName(String name);
}