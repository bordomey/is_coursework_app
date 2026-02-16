CREATE OR REPLACE FUNCTION get_kanban_board(p_project_id INT)
RETURNS TABLE (
    task_id INT,
    title TEXT,
    status TEXT,
    priority TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT t.id, t.title, s.name, p.name
    FROM tasks t
    JOIN task_statuses s ON t.status_id = s.id
    JOIN task_priorities p ON t.priority_id = p.id
    WHERE t.project_id = p_project_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sprint_stats(p_sprint_id INT)
RETURNS TABLE (
    total_tasks INT,
    done_tasks INT
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COUNT(*),
        COUNT(*) FILTER (WHERE status_id =
            (SELECT id FROM task_statuses WHERE name = 'Done'))
    FROM tasks
    WHERE sprint_id = p_sprint_id;
END;
$$ LANGUAGE plpgsql;
