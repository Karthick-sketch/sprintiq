-- PostgreSQL v18.3
DROP SCHEMA public CASCADE;

CREATE SCHEMA public;

-- Users
CREATE TABLE users (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    name varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255),
    role varchar(255) NOT NULL DEFAULT 'USER',
    status varchar(255) NOT NULL DEFAULT 'UNREGISTERED',
    PRIMARY KEY (id),
    CONSTRAINT users_role_check CHECK (
        (role)::text = ANY (
            (
                ARRAY[
                    'USER'::character varying,
                    'ADMIN'::character varying,
                    'SUPER_ADMIN'::character varying
                ]
            )::text []
        )
    ),
    CONSTRAINT users_status_check CHECK (
        (status)::text = ANY (
            (
                ARRAY[
                    'UNREGISTERED'::character varying,
                    'ACTIVE'::character varying,
                    'INACTIVE'::character varying
                ]
            )::text []
        )
    )
);

CREATE UNIQUE INDEX idx_users_email_unique ON public.users USING btree (email);

-- Fields
CREATE TABLE "fields" (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    name varchar(255) NOT NULL,
    description varchar(255),
    "key" varchar(255) NOT NULL,
    "type" varchar(255) NOT NULL DEFAULT 'TEXT',
    kind varchar(255) NOT NULL DEFAULT 'CUSTOM',
    enabled boolean NOT NULL DEFAULT true,
    required boolean NOT NULL DEFAULT false,
    default_value varchar(255),
    PRIMARY KEY (id),
    CONSTRAINT fields_kind_check CHECK (
        (kind)::text = ANY (
            (
                ARRAY[
                    'STANDARD'::character varying,
                    'CUSTOM'::character varying
                ]
            )::text []
        )
    ),
    CONSTRAINT fields_type_check CHECK (
        (type)::text = ANY (
            (
                ARRAY[
                    'TEXT'::character varying,
                    'NUMBER'::character varying,
                    'BOOLEAN'::character varying,
                    'DATE'::character varying,
                    'URL'::character varying,
                    'USER'::character varying,
                    'MULTI_USER'::character varying,
                    'DROPDOWN'::character varying,
                    'MULTI_DROPDOWN'::character varying
                ]
            )::text []
        )
    )
);

-- Projects
CREATE TABLE projects (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    title varchar(255) NOT NULL,
    description varchar(255),
    owner_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_projects_owner_id FOREIGN key (owner_id) REFERENCES users (id)
);

-- Project Users
CREATE TABLE project_users (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    project_id bigint NOT NULL,
    user_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_project_users_project_id FOREIGN key (project_id) REFERENCES projects (id),
    CONSTRAINT fk_project_users_user_id FOREIGN key (user_id) REFERENCES users (id)
);

-- Project Sections
CREATE TABLE sections (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    title varchar(255) NOT NULL,
    order_index integer NOT NULL DEFAULT 0,
    project_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_sections_project_id FOREIGN key (project_id) REFERENCES projects (id)
);

-- Tickets
CREATE TABLE tickets (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    title varchar(255) NOT NULL,
    description varchar(255),
    project_id bigint NOT NULL,
    section_id bigint,
    order_index integer DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_tickets_project_id FOREIGN key (project_id) REFERENCES projects (id),
    CONSTRAINT fk_tickets_section_id FOREIGN key (section_id) REFERENCES sections (id)
);

-- Ticket Fields
CREATE TABLE ticket_fields (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    "value" varchar(255) NOT NULL,
    field_id bigint NOT NULL,
    ticket_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ticket_fields_field_id FOREIGN key (field_id) REFERENCES fields (id),
    CONSTRAINT fk_ticket_fields_ticket_id FOREIGN key (ticket_id) REFERENCES tickets (id)
);

-- Field Templates
CREATE TABLE field_templates (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- Field Template Items
CREATE TABLE field_template_items (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    template_id BIGINT NOT NULL,
    field_id BIGINT NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    required BOOLEAN NOT NULL DEFAULT FALSE,
    order_index INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_template FOREIGN KEY (template_id) REFERENCES field_templates (id),
    CONSTRAINT fk_field FOREIGN KEY (field_id) REFERENCES fields (id)
);

-- Field Options
CREATE TABLE field_options (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    field_id BIGINT NOT NULL,
    value VARCHAR(255) NOT NULL,
    order_index INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_option_field FOREIGN KEY (field_id) REFERENCES fields (id)
);

-- Ticket Comments
CREATE TABLE ticket_comments (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    content varchar(255) NOT NULL,
    created_at timestamp NOT NULL,
    ticket_id bigint NOT NULL,
    created_by bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ticket_comments_ticket_id FOREIGN KEY (ticket_id) REFERENCES tickets (id),
    CONSTRAINT fk_ticket_comments_created_by FOREIGN KEY (created_by) REFERENCES users (id)
);

-- ==================================================
-- Default fields template
-- ==================================================

-- Standard Fields
-- Status and Priority with default values
INSERT INTO
    fields (
        key,
        name,
        description,
        kind,
        type,
        enabled,
        required,
        default_value
    )
VALUES (
        'status',
        'Status',
        'Ticket status',
        'STANDARD',
        'DROPDOWN',
        true,
        true,
        'Open'
    ),
    (
        'priority',
        'Priority',
        'Ticket priority',
        'STANDARD',
        'DROPDOWN',
        true,
        false,
        'Medium'
    );

-- Standard Fields
-- Assignee, Reporter and Due Date without default values
INSERT INTO
    fields (
        key,
        name,
        description,
        kind,
        type,
        enabled,
        required
    )
VALUES (
        'assignee',
        'Assignee',
        'Assigned user',
        'STANDARD',
        'USER',
        true,
        false
    ),
    (
        'reporter',
        'Reporter',
        'Reported by',
        'STANDARD',
        'USER',
        true,
        false
    ),
    (
        'due_date',
        'Due Date',
        'Deadline',
        'STANDARD',
        'DATE',
        true,
        false
    );

-- Default Template
INSERT INTO field_templates (name) VALUES ('Default Template');

-- Template Items
INSERT INTO
    field_template_items (
        template_id,
        field_id,
        enabled,
        required,
        order_index
    )
VALUES (1, 1, true, true, 1), -- Status
    (1, 2, true, false, 2), -- Priority
    (1, 3, true, false, 3), -- Assignee
    (1, 4, true, false, 4), -- Reporter
    (1, 5, true, false, 5);
-- Due Date

-- Status Options
INSERT INTO
    field_options (field_id, value, order_index)
VALUES (1, 'Open', 1),
    (1, 'In Progress', 2),
    (1, 'Done', 3);

-- Priority Options
INSERT INTO
    field_options (field_id, value, order_index)
VALUES (2, 'Low', 1),
    (2, 'Medium', 2),
    (2, 'High', 3),
    (2, 'Urgent', 4);