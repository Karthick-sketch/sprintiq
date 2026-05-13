-- PostgreSQL
-- Dynamic Field Architecture Schema
-- Full reset: drop and recreate everything cleanly.
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- ==================================================
-- Core Tables
-- ==================================================

-- Users
CREATE TABLE users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    role     VARCHAR(50)  NOT NULL DEFAULT 'USER',
    status   VARCHAR(50)  NOT NULL DEFAULT 'UNREGISTERED',
    CONSTRAINT users_role_check CHECK (role IN ('USER', 'ADMIN', 'SUPER_ADMIN')),
    CONSTRAINT users_status_check CHECK (status IN ('UNREGISTERED', 'ACTIVE', 'INACTIVE'))
);
CREATE UNIQUE INDEX idx_users_email ON users (email);

-- Projects
CREATE TABLE projects
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    owner_id    BIGINT,
    CONSTRAINT fk_projects_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);

-- Project Users
CREATE TABLE project_users
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    CONSTRAINT fk_project_users_project FOREIGN KEY (project_id) REFERENCES projects (id),
    CONSTRAINT fk_project_users_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Project Sections
CREATE TABLE sections
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    order_index INTEGER      NOT NULL DEFAULT 0,
    project_id  BIGINT       NOT NULL,
    CONSTRAINT fk_sections_project FOREIGN KEY (project_id) REFERENCES projects (id)
);

-- Tickets
CREATE TABLE tickets
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_by  BIGINT       NOT NULL,
    project_id  BIGINT       NOT NULL,
    section_id  BIGINT,
    parent_id   BIGINT,
    order_index INTEGER DEFAULT 0,
    CONSTRAINT fk_tickets_user FOREIGN KEY (created_by) REFERENCES users (id),
    CONSTRAINT fk_tickets_section FOREIGN KEY (section_id) REFERENCES sections (id),
    CONSTRAINT fk_tickets_project FOREIGN KEY (project_id) REFERENCES projects (id),
    CONSTRAINT fk_tickets_parent FOREIGN KEY (parent_id) REFERENCES tickets (id)
);

-- Ticket Comments
CREATE TABLE ticket_comments
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    content    VARCHAR(2000) NOT NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT NOW(),
    ticket_id  BIGINT        NOT NULL,
    created_by BIGINT        NOT NULL,
    CONSTRAINT fk_comments_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id),
    CONSTRAINT fk_comments_user FOREIGN KEY (created_by) REFERENCES users (id)
);

-- ==================================================
-- Dynamic Field System
-- ==================================================

-- Global field definitions
CREATE TABLE fields
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    system_key    VARCHAR(80) UNIQUE,
    name          VARCHAR(120) NOT NULL,
    description   VARCHAR(500),
    field_kind    VARCHAR(20)  NOT NULL DEFAULT 'CUSTOM',
    field_type    VARCHAR(30)  NOT NULL DEFAULT 'TEXT',
    is_system     BOOLEAN      NOT NULL DEFAULT FALSE,
    is_locked     BOOLEAN      NOT NULL DEFAULT FALSE,
    is_searchable BOOLEAN      NOT NULL DEFAULT TRUE,
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_fields_kind CHECK (field_kind IN ('STANDARD', 'CUSTOM')),
    CONSTRAINT chk_fields_type CHECK (field_type IN (
                                                     'TEXT', 'TEXTAREA', 'NUMBER', 'DATE', 'DATETIME', 'URL',
                                                     'DROPDOWN', 'MULTI_DROPDOWN', 'USER', 'MULTI_USER',
                                                     'CHECKBOX', 'RADIO', 'BOOLEAN'
        ))
);
CREATE INDEX idx_fields_kind_active ON fields (field_kind, is_active);
CREATE INDEX idx_fields_type ON fields (field_type);

-- Global default options for dropdown / radio / status / priority fields
CREATE TABLE field_options
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    field_id              BIGINT       NOT NULL,
    label                 VARCHAR(120) NOT NULL,
    value_key             VARCHAR(120) NOT NULL,
    color                 VARCHAR(30),
    icon                  VARCHAR(80),
    sort_order            INTEGER      NOT NULL DEFAULT 0,
    workflow_semantic_key VARCHAR(30),
    is_default            BOOLEAN      NOT NULL DEFAULT FALSE,
    is_active             BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at            TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_field_options_field FOREIGN KEY (field_id) REFERENCES fields (id),
    CONSTRAINT uk_field_options_value UNIQUE (field_id, value_key),
    CONSTRAINT chk_option_semantic CHECK (
        workflow_semantic_key IS NULL OR workflow_semantic_key IN
                                         ('TODO', 'IN_PROGRESS', 'DONE', 'BLOCKED', 'CANCELLED')
        )
);
CREATE INDEX idx_field_options_field_order ON field_options (field_id, sort_order);
CREATE INDEX idx_field_options_semantic ON field_options (workflow_semantic_key);

-- Project creation presets
CREATE TABLE field_templates
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(120) NOT NULL,
    description  VARCHAR(500),
    template_key VARCHAR(80)  NOT NULL,
    is_default   BOOLEAN      NOT NULL DEFAULT FALSE,
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_field_templates_key UNIQUE (template_key)
);

-- Fields included by a template
CREATE TABLE field_template_items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    template_id BIGINT      NOT NULL,
    field_id    BIGINT      NOT NULL,
    sort_order  INTEGER     NOT NULL DEFAULT 0,
    is_required BOOLEAN     NOT NULL DEFAULT FALSE,
    is_enabled  BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_template_items_template FOREIGN KEY (template_id) REFERENCES field_templates (id),
    CONSTRAINT fk_template_items_field FOREIGN KEY (field_id) REFERENCES fields (id),
    CONSTRAINT uk_template_field UNIQUE (template_id, field_id)
);
CREATE INDEX idx_template_items_order ON field_template_items (template_id, sort_order);

-- Project-level field configuration
CREATE TABLE project_fields
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_id         BIGINT      NOT NULL,
    field_id           BIGINT      NOT NULL,
    display_name       VARCHAR(120),
    help_text          VARCHAR(500),
    sort_order         INTEGER     NOT NULL DEFAULT 0,
    is_enabled         BOOLEAN     NOT NULL DEFAULT TRUE,
    is_required        BOOLEAN     NOT NULL DEFAULT FALSE,
    use_global_options BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_project_fields_project FOREIGN KEY (project_id) REFERENCES projects (id),
    CONSTRAINT fk_project_fields_field FOREIGN KEY (field_id) REFERENCES fields (id),
    CONSTRAINT uk_project_field UNIQUE (project_id, field_id)
);
CREATE INDEX idx_project_fields_project_order ON project_fields (project_id, is_enabled, sort_order);
CREATE INDEX idx_project_fields_field ON project_fields (field_id);

-- Project-specific option overrides (used when use_global_options=FALSE)
CREATE TABLE project_field_options
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_field_id       BIGINT       NOT NULL,
    source_field_option_id BIGINT,
    label                  VARCHAR(120) NOT NULL,
    value_key              VARCHAR(120) NOT NULL,
    color                  VARCHAR(30),
    icon                   VARCHAR(80),
    sort_order             INTEGER      NOT NULL DEFAULT 0,
    workflow_semantic_key  VARCHAR(30),
    is_default             BOOLEAN      NOT NULL DEFAULT FALSE,
    is_active              BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at             TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_pfo_project_field FOREIGN KEY (project_field_id) REFERENCES project_fields (id),
    CONSTRAINT fk_pfo_source_option FOREIGN KEY (source_field_option_id) REFERENCES field_options (id),
    CONSTRAINT uk_project_option_value UNIQUE (project_field_id, value_key),
    CONSTRAINT chk_project_option_semantic CHECK (
        workflow_semantic_key IS NULL OR workflow_semantic_key IN
                                         ('TODO', 'IN_PROGRESS', 'DONE', 'BLOCKED', 'CANCELLED')
        )
);
CREATE INDEX idx_pfo_field_order ON project_field_options (project_field_id, sort_order);
CREATE INDEX idx_pfo_semantic ON project_field_options (workflow_semantic_key);

-- Normalized typed ticket field values (one row per value; multi-values = multiple rows with different value_index)
CREATE TABLE ticket_field_values
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ticket_id               BIGINT      NOT NULL,
    project_field_id        BIGINT      NOT NULL,
    value_index             INTEGER     NOT NULL DEFAULT 0,
    text_value              VARCHAR(2000),
    number_value            NUMERIC(19, 4),
    date_value              DATE,
    datetime_value          TIMESTAMPTZ,
    boolean_value           BOOLEAN,
    field_option_id         BIGINT,
    project_field_option_id BIGINT,
    user_id                 BIGINT,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_tfv_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id) ON DELETE CASCADE,
    CONSTRAINT fk_tfv_project_field FOREIGN KEY (project_field_id) REFERENCES project_fields (id),
    CONSTRAINT fk_tfv_field_option FOREIGN KEY (field_option_id) REFERENCES field_options (id),
    CONSTRAINT fk_tfv_project_field_option FOREIGN KEY (project_field_option_id) REFERENCES project_field_options (id),
    CONSTRAINT fk_tfv_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uk_tfv_row UNIQUE (ticket_id, project_field_id, value_index),
    CONSTRAINT chk_tfv_single_value CHECK (
        (CASE WHEN text_value IS NOT NULL THEN 1 ELSE 0 END +
         CASE WHEN number_value IS NOT NULL THEN 1 ELSE 0 END +
         CASE WHEN date_value IS NOT NULL THEN 1 ELSE 0 END +
         CASE WHEN datetime_value IS NOT NULL THEN 1 ELSE 0 END +
         CASE WHEN boolean_value IS NOT NULL THEN 1 ELSE 0 END +
         CASE WHEN field_option_id IS NOT NULL THEN 1 ELSE 0 END +
         CASE WHEN project_field_option_id IS NOT NULL THEN 1 ELSE 0 END +
         CASE WHEN user_id IS NOT NULL THEN 1 ELSE 0 END) = 1
        )
);
CREATE INDEX idx_tfv_ticket_field ON ticket_field_values (ticket_id, project_field_id);
CREATE INDEX idx_tfv_field_option ON ticket_field_values (project_field_id, field_option_id);
CREATE INDEX idx_tfv_project_option ON ticket_field_values (project_field_id, project_field_option_id);
CREATE INDEX idx_tfv_user ON ticket_field_values (project_field_id, user_id);
CREATE INDEX idx_tfv_number ON ticket_field_values (project_field_id, number_value);
CREATE INDEX idx_tfv_date ON ticket_field_values (project_field_id, date_value);
CREATE INDEX idx_tfv_datetime ON ticket_field_values (project_field_id, datetime_value);
CREATE INDEX idx_tfv_boolean ON ticket_field_values (project_field_id, boolean_value);

-- Legacy: kept for backward compatibility — deprecated in favor of ticket_field_values
CREATE TABLE ticket_fields
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    value     VARCHAR(255) NOT NULL,
    field_id  BIGINT       NOT NULL,
    ticket_id BIGINT       NOT NULL,
    CONSTRAINT fk_ticket_fields_field FOREIGN KEY (field_id) REFERENCES fields (id),
    CONSTRAINT fk_ticket_fields_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);

-- ==================================================
-- Seed: Standard Fields
-- ==================================================
INSERT INTO fields (system_key, name, description, field_kind, field_type, is_system, is_locked, is_searchable)
VALUES ('status', 'Status', 'Workflow state of the ticket', 'STANDARD', 'DROPDOWN', TRUE, TRUE, TRUE),
       ('priority', 'Priority', 'Business urgency of the ticket', 'STANDARD', 'DROPDOWN', TRUE, TRUE, TRUE),
       ('assignee', 'Assignee', 'User responsible for the ticket', 'STANDARD', 'USER', TRUE, TRUE, TRUE),
       ('reporter', 'Reporter', 'User who created or reported the ticket', 'STANDARD', 'USER', TRUE, TRUE, TRUE),
       ('due_date', 'Due Date', 'Target completion date', 'STANDARD', 'DATE', TRUE, TRUE, TRUE),
       ('story_points', 'Story Points', 'Relative delivery estimate', 'STANDARD', 'NUMBER', TRUE, FALSE, TRUE),
       ('github_url', 'GitHub URL', 'Related pull request or issue URL', 'STANDARD', 'URL', TRUE, FALSE, TRUE);

-- ==================================================
-- Seed: Status Options
-- ==================================================
INSERT INTO field_options (field_id, label, value_key, color, sort_order, workflow_semantic_key, is_default)
SELECT id, 'Backlog', 'backlog', '#6B7280', 10, 'TODO', TRUE
FROM fields
WHERE system_key = 'status'
UNION ALL
SELECT id, 'To Do', 'todo', '#2563EB', 20, 'TODO', FALSE
FROM fields
WHERE system_key = 'status'
UNION ALL
SELECT id, 'In Progress', 'in_progress', '#D97706', 30, 'IN_PROGRESS', FALSE
FROM fields
WHERE system_key = 'status'
UNION ALL
SELECT id, 'Blocked', 'blocked', '#DC2626', 40, 'BLOCKED', FALSE
FROM fields
WHERE system_key = 'status'
UNION ALL
SELECT id, 'Done', 'done', '#16A34A', 50, 'DONE', FALSE
FROM fields
WHERE system_key = 'status'
UNION ALL
SELECT id, 'Cancelled', 'cancelled', '#71717A', 60, 'CANCELLED', FALSE
FROM fields
WHERE system_key = 'status';

-- ==================================================
-- Seed: Priority Options
-- ==================================================
INSERT INTO field_options (field_id, label, value_key, color, sort_order, is_default)
SELECT id, 'Low', 'low', '#22C55E', 10, FALSE
FROM fields
WHERE system_key = 'priority'
UNION ALL
SELECT id, 'Medium', 'medium', '#3B82F6', 20, TRUE
FROM fields
WHERE system_key = 'priority'
UNION ALL
SELECT id, 'High', 'high', '#F97316', 30, FALSE
FROM fields
WHERE system_key = 'priority'
UNION ALL
SELECT id, 'Urgent', 'urgent', '#EF4444', 40, FALSE
FROM fields
WHERE system_key = 'priority';

-- ==================================================
-- Seed: Default Template
-- ==================================================
INSERT INTO field_templates (name, description, template_key, is_default)
VALUES ('Default Template', 'General project field set', 'default', TRUE);

INSERT INTO field_template_items (template_id, field_id, sort_order, is_required, is_enabled)
SELECT t.id,
       f.id,
       CASE f.system_key
           WHEN 'status' THEN 10
           WHEN 'priority' THEN 20
           WHEN 'assignee' THEN 30
           WHEN 'reporter' THEN 40
           WHEN 'due_date' THEN 50
           WHEN 'story_points' THEN 60
           WHEN 'github_url' THEN 70
           ELSE 100
           END,
       CASE WHEN f.system_key IN ('status', 'priority', 'reporter') THEN TRUE ELSE FALSE END,
       TRUE
FROM field_templates t
         JOIN fields f ON f.system_key IN
                          ('status', 'priority', 'assignee', 'reporter', 'due_date', 'story_points', 'github_url')
WHERE t.template_key = 'default';
