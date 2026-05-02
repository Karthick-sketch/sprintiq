-- Sample Data Insertions

-- Users // passwords -> [password1, password2, password3, password4, password5]
INSERT INTO
    users (
        name,
        email,
        password,
        role,
        status
    )
VALUES (
        'User One',
        'user1@example.com',
        '$2a$10$Wc/NilU/bM06bYFQgEeEQOJmnTchC/LxFfjQoLCl2ks1vYFmMqpgm',
        'SUPER_ADMIN',
        'ACTIVE'
    ),
    (
        'User Two',
        'user2@example.com',
        '$2a$10$lHFGOYuMde0O0kdE3bivh.lF1n6nrx9j97U0Z41XTC5rcCKSrKJoO',
        'ADMIN',
        'ACTIVE'
    ),
    (
        'User Three',
        'user3@example.com',
        '$2a$10$..ozEwiQNHiQpfW0d2UeKOJhuxm8Z.9kxdm8Bi7QqVTWm3iz7XAkW',
        'USER',
        'ACTIVE'
    ),
    (
        'User Four',
        'user4@example.com',
        '$2a$10$naOLbjFCSRvU6I5dt.8x6udgPmheSIZ5gc3N01xDmAx4ztCb4MZCS',
        'USER',
        'ACTIVE'
    ),
    (
        'User Five',
        'user5@example.com',
        '$2a$10$wA57jqSO93ipYfoipT/ViuViH9oCxgd7X2RfrAe9DIWjLu1ozULVC',
        'USER',
        'INACTIVE'
    );

-- Custom Fields
INSERT INTO
    fields (
        name,
        description,
        "key",
        "type",
        kind,
        enabled,
        required
    )
VALUES (
        'Customer Name',
        'Customer Name',
        'customer_name',
        'TEXT',
        'CUSTOM',
        true,
        true
    ),
    (
        'Reviewer',
        'Reviewer',
        'reviewer',
        'USER',
        'CUSTOM',
        true,
        false
    ),
    (
        'GitHub Link',
        'GitHub Link',
        'github_link',
        'URL',
        'CUSTOM',
        true,
        false
    );

-- Projects
INSERT INTO
    projects (title, description, owner_id)
VALUES (
        'Project Alpha',
        'First sample project',
        1
    ),
    (
        'Project Beta',
        'Second sample project',
        2
    ),
    (
        'Project Gamma',
        'Third sample project',
        3
    ),
    (
        'Project Delta',
        'Fourth sample project',
        4
    ),
    (
        'Project Epsilon',
        'Fifth sample project',
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
        title,
        project_id,
        order_index
    )
VALUES ('To Do', 1, 1),
    ('In Progress', 1, 2),
    ('Done', 1, 3),
    ('Backlog', 2, 1),
    ('Development', 2, 2);

-- Tickets
INSERT INTO
    tickets (
        title,
        description,
        project_id,
        section_id,
        order_index
    )
VALUES (
        'Fix bug A',
        'Ticket 1 description',
        1,
        1,
        1
    ),
    (
        'Implement feature B',
        'Ticket 2 description',
        1,
        2,
        2
    ),
    (
        'Update docs',
        'Ticket 3 description',
        1,
        3,
        1
    ),
    (
        'Security patch',
        'Ticket 4 description',
        2,
        4,
        1
    ),
    (
        'Refactor code',
        'Ticket 5 description',
        2,
        5,
        2
    );

-- Ticket Fields (Combining standard fields from original tickets insert + custom fields)
INSERT INTO
    ticket_fields ("value", field_id, ticket_id)
VALUES
    -- Ticket 1 standard fields
    ('Open', 1, 1),
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
    ('Open', 1, 4),
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
    -- Ticket Custom Fields (6: Customer Name, 7: Reviewer, 8: GitHub Link)
    ('Acme Corp', 6, 1),
    ('3', 7, 2),
    (
        'http://github.com/example/repo',
        8,
        3
    ),
    ('2', 7, 4),
    ('Initech', 6, 5);

-- Ticket Comments
INSERT INTO
    ticket_comments (
        content,
        created_at,
        ticket_id,
        created_by
    )
VALUES (
        'Can you please provide more details on this bug?',
        CURRENT_TIMESTAMP,
        1,
        1
    ),
    (
        'I have started working on this feature.',
        CURRENT_TIMESTAMP,
        2,
        2
    ),
    (
        'The documentation has been successfully updated.',
        CURRENT_TIMESTAMP,
        3,
        3
    ),
    (
        'Applying the security patch right now. Expect downtime.',
        CURRENT_TIMESTAMP,
        4,
        1
    ),
    (
        'Code refactored and tests are passing.',
        CURRENT_TIMESTAMP,
        5,
        2
    );