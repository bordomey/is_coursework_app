package com.game.track.repository;

import com.game.track.dto.KanbanTaskDto;

import java.util.List;

public interface CustomProjectRepository {
    List<KanbanTaskDto> getKanbanBoard(Integer projectId);
}