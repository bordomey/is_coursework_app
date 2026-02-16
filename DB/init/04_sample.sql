INSERT INTO roles(name) VALUES
('Developer'), ('TeamLead'), ('ProjectManager');

INSERT INTO task_statuses(name) VALUES
('To Do'), ('In Progress'), ('Done');

INSERT INTO task_priorities(name) VALUES
('Low'), ('Medium'), ('High');

INSERT INTO users(email, full_name, password_hash)
VALUES ('dev@test.com', 'Ivan Dev', 'hash');

INSERT INTO projects(name)
VALUES ('Awesome Game');

INSERT INTO project_members VALUES (1, 1);

INSERT INTO sprints(project_id, name)
VALUES (1, 'Sprint 1');

INSERT INTO tasks(project_id, sprint_id, title, status_id, priority_id)
VALUES (1, 1, 'Implement movement', 1, 3);
