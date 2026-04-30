DROP SCHEMA public CASCADE;

CREATE SCHEMA public;

-- Users
CREATE TABLE users (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    created_at timestamp without time zone,
    email varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    password varchar(255),
    role varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
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

-- Custom Fields
CREATE TABLE custom_fields (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    default_value varchar(255),
    description varchar(255),
    enabled boolean,
    "key" varchar(255) NOT NULL,
    kind varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    required boolean,
    "type" varchar(255) NOT NULL,
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
    description varchar(255),
    title varchar(255) NOT NULL,
    owner_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_projects_owner_id FOREIGN key (owner_id) REFERENCES users (id)
);

-- Project Users
CREATE TABLE project_users (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    project_id bigint,
    user_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_project_users_project_id FOREIGN key (project_id) REFERENCES projects (id),
    CONSTRAINT fk_project_users_user_id FOREIGN key (user_id) REFERENCES users (id)
);

-- Project Sections
CREATE TABLE sections (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    order_index integer NOT NULL,
    title varchar(255) NOT NULL,
    project_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_sections_project_id FOREIGN key (project_id) REFERENCES projects (id)
);

-- Tickets
CREATE TABLE tickets (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    description varchar(255),
    due_date date,
    order_index integer NOT NULL,
    priority varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
    title varchar(255) NOT NULL,
    assignee_id bigint,
    reporter_id bigint,
    section_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_tickets_assignee_id FOREIGN key (assignee_id) REFERENCES users (id),
    CONSTRAINT fk_tickets_reporter_id FOREIGN key (reporter_id) REFERENCES users (id),
    CONSTRAINT fk_tickets_section_id FOREIGN key (section_id) REFERENCES sections (id),
    CONSTRAINT tickets_priority_check CHECK (
        (priority)::text = ANY (
            (
                ARRAY[
                    'LOW'::character varying,
                    'MEDIUM'::character varying,
                    'HIGH'::character varying,
                    'URGENT'::character varying
                ]
            )::text []
        )
    ),
    CONSTRAINT tickets_status_check CHECK (
        (status)::text = ANY (
            (
                ARRAY[
                    'TODO'::character varying,
                    'IN_PROGRESS'::character varying,
                    'DONE'::character varying
                ]
            )::text []
        )
    )
);

-- Ticket Custom Fields
CREATE TABLE ticket_custom_fields (
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    "value" varchar(255),
    custom_field_id bigint,
    ticket_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_ticket_custom_fields_custom_field_id FOREIGN key (custom_field_id) REFERENCES custom_fields (id),
    CONSTRAINT fk_ticket_custom_fields_ticket_id FOREIGN key (ticket_id) REFERENCES tickets (id)
);

-- Sample Data Insertions

-- Users // passwords -> [password1, password2, password3, password4, password5]
INSERT INTO
    users (
        created_at,
        email,
        name,
        password,
        role,
        status
    )
VALUES (
        CURRENT_TIMESTAMP,
        'user1@example.com',
        'User One',
        '$2a$10$Wc/NilU/bM06bYFQgEeEQOJmnTchC/LxFfjQoLCl2ks1vYFmMqpgm',
        'SUPER_ADMIN',
        'ACTIVE'
    ),
    (
        CURRENT_TIMESTAMP,
        'user2@example.com',
        'User Two',
        '$2a$10$lHFGOYuMde0O0kdE3bivh.lF1n6nrx9j97U0Z41XTC5rcCKSrKJoO',
        'ADMIN',
        'ACTIVE'
    ),
    (
        CURRENT_TIMESTAMP,
        'user3@example.com',
        'User Three',
        '$2a$10$..ozEwiQNHiQpfW0d2UeKOJhuxm8Z.9kxdm8Bi7QqVTWm3iz7XAkW',
        'USER',
        'ACTIVE'
    ),
    (
        CURRENT_TIMESTAMP,
        'user4@example.com',
        'User Four',
        '$2a$10$naOLbjFCSRvU6I5dt.8x6udgPmheSIZ5gc3N01xDmAx4ztCb4MZCS',
        'USER',
        'ACTIVE'
    ),
    (
        CURRENT_TIMESTAMP,
        'user5@example.com',
        'User Five',
        '$2a$10$wA57jqSO93ipYfoipT/ViuViH9oCxgd7X2RfrAe9DIWjLu1ozULVC',
        'USER',
        'INACTIVE'
    );

-- Custom Fields
INSERT INTO
    custom_fields (
        default_value,
        description,
        enabled,
        "key",
        kind,
        name,
        required,
        "type"
    )
VALUES (
        '0',
        'Story points',
        true,
        'story_points',
        'STANDARD',
        'Story Points',
        false,
        'NUMBER'
    ),
    (
        '',
        'Customer name',
        true,
        'customer_name',
        'CUSTOM',
        'Customer',
        false,
        'TEXT'
    ),
    (
        'false',
        'Is escalated',
        true,
        'is_escalated',
        'CUSTOM',
        'Escalated',
        false,
        'BOOLEAN'
    ),
    (
        '',
        'External link',
        true,
        'external_link',
        'CUSTOM',
        'Link',
        false,
        'URL'
    ),
    (
        '',
        'Due date',
        true,
        'due_date_cf',
        'STANDARD',
        'Target Date',
        false,
        'DATE'
    );

-- Projects
INSERT INTO
    projects (description, title, owner_id)
VALUES (
        'First sample project',
        'Project Alpha',
        1
    ),
    (
        'Second sample project',
        'Project Beta',
        2
    ),
    (
        'Third sample project',
        'Project Gamma',
        3
    ),
    (
        'Fourth sample project',
        'Project Delta',
        4
    ),
    (
        'Fifth sample project',
        'Project Epsilon',
        1
    );

-- Project Users
INSERT INTO
    project_users (project_id, user_id)
VALUES (1, 1),
    (1, 2),
    (2, 2),
    (2, 3),
    (3, 4);

-- Sections
INSERT INTO
    sections (
        order_index,
        title,
        project_id
    )
VALUES (1, 'To Do', 1),
    (2, 'In Progress', 1),
    (3, 'Done', 1),
    (1, 'Backlog', 2),
    (2, 'Development', 2);

-- Tickets
INSERT INTO
    tickets (
        description,
        due_date,
        order_index,
        priority,
        status,
        title,
        assignee_id,
        reporter_id,
        section_id
    )
VALUES (
        'Ticket 1 description',
        '2026-05-01',
        1,
        'HIGH',
        'TODO',
        'Fix bug A',
        2,
        1,
        1
    ),
    (
        'Ticket 2 description',
        '2026-05-02',
        2,
        'MEDIUM',
        'IN_PROGRESS',
        'Implement feature B',
        2,
        1,
        2
    ),
    (
        'Ticket 3 description',
        '2026-05-03',
        1,
        'LOW',
        'DONE',
        'Update docs',
        3,
        2,
        3
    ),
    (
        'Ticket 4 description',
        '2026-05-04',
        1,
        'URGENT',
        'TODO',
        'Security patch',
        4,
        1,
        4
    ),
    (
        'Ticket 5 description',
        '2026-05-05',
        2,
        'MEDIUM',
        'IN_PROGRESS',
        'Refactor code',
        1,
        2,
        5
    );

-- Ticket Custom Fields
INSERT INTO
    ticket_custom_fields (
        "value",
        custom_field_id,
        ticket_id
    )
VALUES ('5', 1, 1),
    ('Acme Corp', 2, 1),
    ('true', 3, 2),
    ('http://example.com', 4, 3),
    ('2026-06-01', 5, 4);