INSERT INTO users (email, full_name, password_hash)
SELECT
    'user' || i || '@test.com',
    'User ' || i,
    md5(random()::text)
FROM generate_series(1, 100000) i
ON CONFLICT DO NOTHING;
INSERT INTO projects (name, description)
SELECT
    'Project ' || i,
    'Game project #' || i
FROM generate_series(1, 1000) i
ON CONFLICT DO NOTHING;
INSERT INTO project_members (project_id, user_id)
SELECT 
    (random() * 999 + 1)::int,
    (random() * 99999 + 1)::int
FROM generate_series(1, 500000)
ON CONFLICT DO NOTHING;
INSERT INTO sprints (project_id, name, start_date, end_date)
SELECT
    (random() * 999 + 1)::int,
    'Sprint ' || i,
    now()::date - (random() * 100)::int,
    now()::date + (random() * 30)::int
FROM generate_series(1, 5000) i;
INSERT INTO tasks (
    project_id,
    sprint_id,
    title,
    description,
    status_id,
    priority_id,
    created_at
)
SELECT
    (random() * 999 + 1)::int,
    (random() * 4999 + 1)::int,
    'Task #' || i,
    repeat('Task description ', 10),
    (random() * 2 + 1)::int,
    (random() * 2 + 1)::int,
    now() - (random() * interval '180 days')
FROM generate_series(1, 10000000) i
ON CONFLICT DO NOTHING;
INSERT INTO task_assignees (task_id, user_id)
SELECT
    (random() * 9999999 + 1)::int,
    (random() * 99999 + 1)::int
FROM generate_series(1, 15000000)
ON CONFLICT DO NOTHING;
INSERT INTO comments (task_id, user_id, content, created_at)
SELECT
    (random() * 9999999 + 1)::int,
    (random() * 99999 + 1)::int,
    repeat('Comment ', 20),
    now() - (random() * interval '90 days')
FROM generate_series(1, 10000000)
ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (task_id, old_status_id, new_status_id, changed_at)
SELECT
    (random() * 9999999 + 1)::int,
    (random() * 2 + 1)::int,
    (random() * 2 + 1)::int,
    now() - (random() * interval '120 days')
FROM generate_series(1, 25000000)
ON CONFLICT DO NOTHING;
