-- Sample Data Insertions

-- Users // passwords -> [password1, password2, password3, password4, password5]
INSERT INTO
    users (
        email,
        name,
        password,
        role,
        status
    )
VALUES (
        'user1@example.com',
        'User One',
        '$2a$10$Wc/NilU/bM06bYFQgEeEQOJmnTchC/LxFfjQoLCl2ks1vYFmMqpgm',
        'SUPER_ADMIN',
        'ACTIVE'
    ),
    (
        'user2@example.com',
        'User Two',
        '$2a$10$lHFGOYuMde0O0kdE3bivh.lF1n6nrx9j97U0Z41XTC5rcCKSrKJoO',
        'ADMIN',
        'ACTIVE'
    ),
    (
        'user3@example.com',
        'User Three',
        '$2a$10$..ozEwiQNHiQpfW0d2UeKOJhuxm8Z.9kxdm8Bi7QqVTWm3iz7XAkW',
        'USER',
        'ACTIVE'
    ),
    (
        'user4@example.com',
        'User Four',
        '$2a$10$naOLbjFCSRvU6I5dt.8x6udgPmheSIZ5gc3N01xDmAx4ztCb4MZCS',
        'USER',
        'ACTIVE'
    ),
    (
        'user5@example.com',
        'User Five',
        '$2a$10$wA57jqSO93ipYfoipT/ViuViH9oCxgd7X2RfrAe9DIWjLu1ozULVC',
        'USER',
        'INACTIVE'
    );

-- Custom Fields
INSERT INTO
    fields (
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
        order_index,
        title,
        project_id,
        section_id
    )
VALUES (
        'Ticket 1 description',
        1,
        'Fix bug A',
        1,
        1
    ),
    (
        'Ticket 2 description',
        2,
        'Implement feature B',
        1,
        2
    ),
    (
        'Ticket 3 description',
        1,
        'Update docs',
        1,
        3
    ),
    (
        'Ticket 4 description',
        1,
        'Security patch',
        2,
        4
    ),
    (
        'Ticket 5 description',
        2,
        'Refactor code',
        2,
        5
    );

-- Ticket Fields (Combining standard fields from original tickets insert + custom fields)
INSERT INTO
    ticket_fields (
        "value",
        field_id,
        ticket_id
    )
VALUES 
    -- Ticket 1 standard fields
    ('To Do', 1, 1),
    ('High', 2, 1),
    ('2', 3, 1),
    ('1', 4, 1),
    ('2026-05-01', 5, 1),
    -- Ticket 2 standard fields
    ('In Progress', 1, 2),
    ('Medium', 2, 2),
    ('2', 3, 2),
    ('1', 4, 2),
    ('2026-05-02', 5, 2),
    -- Ticket 3 standard fields
    ('Done', 1, 3),
    ('Low', 2, 3),
    ('3', 3, 3),
    ('2', 4, 3),
    ('2026-05-03', 5, 3),
    -- Ticket 4 standard fields
    ('To Do', 1, 4),
    ('Urgent', 2, 4),
    ('4', 3, 4),
    ('1', 4, 4),
    ('2026-05-04', 5, 4),
    -- Ticket 5 standard fields
    ('In Progress', 1, 5),
    ('Medium', 2, 5),
    ('1', 3, 5),
    ('2', 4, 5),
    ('2026-05-05', 5, 5),
    -- Ticket Custom Fields (mapped IDs: 1->6, 2->7, 3->8, 4->9, 5->10)
    ('5', 6, 1),
    ('Acme Corp', 7, 1),
    ('true', 8, 2),
    ('http://example.com', 9, 3),
    ('2026-06-01', 10, 4);