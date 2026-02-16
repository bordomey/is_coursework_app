CREATE OR REPLACE FUNCTION log_task_status_change()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.status_id IS DISTINCT FROM NEW.status_id THEN
        INSERT INTO audit_logs(task_id, old_status_id, new_status_id)
        VALUES (OLD.id, OLD.status_id, NEW.status_id);
    END IF;
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_task_status_change
BEFORE UPDATE ON tasks
FOR EACH ROW
EXECUTE FUNCTION log_task_status_change();
