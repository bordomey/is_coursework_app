package com.game.track.repository;

import com.game.track.dto.KanbanTaskDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class CustomProjectRepositoryImpl implements CustomProjectRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<KanbanTaskDto> getKanbanBoard(Integer projectId) {
        String sql = "SELECT * FROM get_kanban_board(:projectId)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projectId", projectId);

        List<Object[]> results = query.getResultList();
        List<KanbanTaskDto> kanbanTasks = new ArrayList<>();

        for (Object[] row : results) {
            KanbanTaskDto task = new KanbanTaskDto(
                (Integer) row[0],  // task_id
                (String) row[1],   // title
                (String) row[2],   // status
                (String) row[3]    // priority
            );
            kanbanTasks.add(task);
        }

        return kanbanTasks;
    }
}