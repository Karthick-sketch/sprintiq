-- Sample Data Insertions

-- Users // passwords -> [password1, password2, password3, password4, password5]
INSERT INTO users (name,
                   email,
                   password,
                   role,
                   status)
VALUES ('User One',
        'user1@example.com',
        '$2a$10$Wc/NilU/bM06bYFQgEeEQOJmnTchC/LxFfjQoLCl2ks1vYFmMqpgm',
        'SUPER_ADMIN',
        'ACTIVE'),
       ('User Two',
        'user2@example.com',
        '$2a$10$lHFGOYuMde0O0kdE3bivh.lF1n6nrx9j97U0Z41XTC5rcCKSrKJoO',
        'ADMIN',
        'ACTIVE'),
       ('User Three',
        'user3@example.com',
        '$2a$10$..ozEwiQNHiQpfW0d2UeKOJhuxm8Z.9kxdm8Bi7QqVTWm3iz7XAkW',
        'USER',
        'ACTIVE'),
       ('User Four',
        'user4@example.com',
        '$2a$10$naOLbjFCSRvU6I5dt.8x6udgPmheSIZ5gc3N01xDmAx4ztCb4MZCS',
        'USER',
        'ACTIVE'),
       ('User Five',
        'user5@example.com',
        '$2a$10$wA57jqSO93ipYfoipT/ViuViH9oCxgd7X2RfrAe9DIWjLu1ozULVC',
        'USER',
        'INACTIVE');

-- Custom Fields
INSERT INTO fields (name,
                    description,
                    system_key,
                    field_type,
                    field_kind,
                    is_active)
VALUES ('Customer Name',
        'Customer Name',
        'customer_name',
        'TEXT',
        'CUSTOM',
        true),
       ('Reviewer',
        'Reviewer',
        'reviewer',
        'USER',
        'CUSTOM',
        true),
       ('GitHub Link',
        'GitHub Link',
        'github_link_custom',
        'URL',
        'CUSTOM',
        true);

-- Projects
INSERT INTO projects (title, description, owner_id)
VALUES ('Project Alpha',
        'First sample project',
        1),
       ('Project Beta',
        'Second sample project',
        2),
       ('Project Gamma',
        'Third sample project',
        3),
       ('Project Delta',
        'Fourth sample project',
        4),
       ('Project Epsilon',
        'Fifth sample project',
        1);

-- Project Users
INSERT INTO project_users (project_id, user_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 4);

-- Sections
INSERT INTO sections (title,
                      project_id,
                      order_index)
VALUES ('To Do', 1, 1),
       ('In Progress', 1, 2),
       ('Done', 1, 3),
       ('Backlog', 2, 1),
       ('Development', 2, 2);

-- Tickets
INSERT INTO tickets (title,
                     description,
                     created_by,
                     project_id,
                     section_id,
                     order_index)
VALUES ('Fix bug A',
        'Ticket 1 description',
        1,
        1,
        1,
        1),
       ('Implement feature B',
        'Ticket 2 description',
        2,
        1,
        2,
        2),
       ('Update docs',
        'Ticket 3 description',
        3,
        1,
        3,
        1),
       ('Security patch',
        'Ticket 4 description',
        1,
        2,
        4,
        1),
       ('Refactor code',
        'Ticket 5 description',
        2,
        2,
        5,
        2);

-- Project Fields (Project 1 & 2)
INSERT INTO project_fields (project_id, field_id, display_name, sort_order, is_enabled, is_required)
VALUES
    -- Project 1 (IDs 1-8)
    (1, 1, 'Status', 10, true, true),
    (1, 2, 'Priority', 20, true, true),
    (1, 3, 'Assignee', 30, true, false),
    (1, 4, 'Reporter', 40, true, true),
    (1, 5, 'Due Date', 50, true, false),
    (1, 8, 'Customer Name', 60, true, true),
    (1, 9, 'Reviewer', 70, true, false),
    (1, 10, 'GitHub Link', 80, true, false),
    -- Project 2 (IDs 9-16)
    (2, 1, 'Status', 10, true, true),
    (2, 2, 'Priority', 20, true, true),
    (2, 3, 'Assignee', 30, true, false),
    (2, 4, 'Reporter', 40, true, true),
    (2, 5, 'Due Date', 50, true, false),
    (2, 8, 'Customer Name', 60, true, true),
    (2, 9, 'Reviewer', 70, true, false),
    (2, 10, 'GitHub Link', 80, true, false);

-- Ticket Fields (Combining standard fields from original tickets insert + custom fields)
INSERT INTO ticket_fields ("value", field_id, ticket_id)
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
    -- Ticket Custom Fields (8: Customer Name, 9: Reviewer, 10: GitHub Link)
    ('Acme Corp', 8, 1),
    ('3', 9, 2),
    ('http://github.com/example/repo',
     10,
     3),
    ('2', 9, 4),
    ('Initech', 8, 5);

-- Ticket Field Values (Normalized Data)
INSERT INTO ticket_field_values (ticket_id, project_field_id, field_option_id, user_id, date_value, text_value)
VALUES
    -- Ticket 1 (Project 1)
    (1, 1, 2, NULL, NULL, NULL), -- Status: To Do
    (1, 2, 9, NULL, NULL, NULL), -- Priority: High
    (1, 3, NULL, 2, NULL, NULL), -- Assignee: User 2
    (1, 4, NULL, 1, NULL, NULL), -- Reporter: User 1
    (1, 5, NULL, NULL, '2026-05-01', NULL), -- Due Date
    (1, 6, NULL, NULL, NULL, 'Acme Corp'), -- Customer Name

    -- Ticket 2 (Project 1)
    (2, 1, 3, NULL, NULL, NULL), -- Status: In Progress
    (2, 2, 8, NULL, NULL, NULL), -- Priority: Medium
    (2, 3, NULL, 2, NULL, NULL), -- Assignee
    (2, 4, NULL, 1, NULL, NULL), -- Reporter
    (2, 5, NULL, NULL, '2026-05-02', NULL), -- Due Date
    (2, 7, NULL, 3, NULL, NULL), -- Reviewer

    -- Ticket 3 (Project 1)
    (3, 1, 5, NULL, NULL, NULL), -- Status: Done
    (3, 2, 7, NULL, NULL, NULL), -- Priority: Low
    (3, 3, NULL, 3, NULL, NULL), -- Assignee
    (3, 4, NULL, 2, NULL, NULL), -- Reporter
    (3, 5, NULL, NULL, '2026-05-03', NULL), -- Due Date
    (3, 8, NULL, NULL, NULL, 'http://github.com/example/repo'), -- GitHub Link

    -- Ticket 4 (Project 2)
    (4, 9, 2, NULL, NULL, NULL), -- Status: To Do
    (4, 10, 10, NULL, NULL, NULL), -- Priority: Urgent
    (4, 11, NULL, 4, NULL, NULL), -- Assignee
    (4, 12, NULL, 1, NULL, NULL), -- Reporter
    (4, 13, NULL, NULL, '2026-05-04', NULL), -- Due Date
    (4, 15, NULL, 2, NULL, NULL), -- Reviewer

    -- Ticket 5 (Project 2)
    (5, 9, 3, NULL, NULL, NULL), -- Status: In Progress
    (5, 10, 8, NULL, NULL, NULL), -- Priority: Medium
    (5, 11, NULL, 1, NULL, NULL), -- Assignee
    (5, 12, NULL, 2, NULL, NULL), -- Reporter
    (5, 13, NULL, NULL, '2026-05-05', NULL), -- Due Date
    (5, 14, NULL, NULL, NULL, 'Initech'); -- Customer Name

-- Ticket Comments
INSERT INTO ticket_comments (content,
                             created_at,
                             ticket_id,
                             created_by)
VALUES ('Can you please provide more details on this bug?',
        CURRENT_TIMESTAMP,
        1,
        1),
       ('I have started working on this feature.',
        CURRENT_TIMESTAMP,
        2,
        2),
       ('The documentation has been successfully updated.',
        CURRENT_TIMESTAMP,
        3,
        3),
       ('Applying the security patch right now. Expect downtime.',
        CURRENT_TIMESTAMP,
        4,
        1),
       ('Code refactored and tests are passing.',
        CURRENT_TIMESTAMP,
        5,
        2);
