```mermaid
erDiagram
	 
    USER {
        int id PK
        text email
        text full_name
        text password_hash
        timestamp created_at
    }

    ROLE {
        int id PK
        text name
    }

    USER_ROLE {
        int user_id FK
        int role_id FK
    }

    PROJECT {
        int id PK
        text name
        text description
        boolean is_archived
        timestamp created_at
    }

    PROJECT_MEMBER {
        int project_id FK
        int user_id FK
    }

    SPRINT {
        int id PK
        int project_id FK
        text name
        date start_date
        date end_date
    }

    TASK {
        int id PK
        int project_id FK
        int sprint_id FK
        int status_id FK
        int priority_id FK
        text title
        text description
        timestamp created_at
        timestamp updated_at
    }

    TASK_STATUS {
        int id PK
        text name
    }

    TASK_PRIORITY {
        int id PK
        text name
    }

    TASK_ASSIGNEE {
        int task_id FK
        int user_id FK
    }

    COMMENT {
        int id PK
        int task_id FK
        int user_id FK
        text content
        timestamp created_at
    }

    AUDIT_LOG {
        int id PK
        int task_id FK
        int old_status_id
        int new_status_id
        timestamp changed_at
    }
	
    USER ||--o{ USER_ROLE : has
    ROLE ||--o{ USER_ROLE : assigned_to

    USER ||--o{ PROJECT_MEMBER : participates
    PROJECT ||--o{ PROJECT_MEMBER : includes

    PROJECT ||--o{ SPRINT : has
    PROJECT ||--o{ TASK : contains

    SPRINT ||--o{ TASK : includes

    TASK_STATUS ||--o{ TASK : defines
    TASK_PRIORITY ||--o{ TASK : defines

    USER ||--o{ TASK_ASSIGNEE : assigned
    TASK ||--o{ TASK_ASSIGNEE : has

    TASK ||--o{ COMMENT : has
    USER ||--o{ COMMENT : writes

    TASK ||--o{ AUDIT_LOG : tracks
