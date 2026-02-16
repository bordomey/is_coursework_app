package com.game.track.repository;

import com.game.track.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer>, CustomProjectRepository {
    List<Project> findByIsArchivedFalse();

    @Modifying
    @Query("UPDATE Project p SET p.isArchived = true WHERE p.id = :id")
    void archiveProject(@Param("id") Integer id);
    
    @Query(value = "SELECT * FROM projects p WHERE p.id IN " +
           "(SELECT pm.project_id FROM project_members pm WHERE pm.user_id = :userId)", 
           nativeQuery = true)
    List<Project> findByUserId(@Param("userId") Integer userId);
}