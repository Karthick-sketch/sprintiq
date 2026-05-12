package com.karthi.sprintiq.fields.entity;

import com.karthi.sprintiq.projects.entity.Project;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

/**
 * Project-level field configuration.
 * Decides whether a global FieldDefinition is active in a project,
 * required, ordered, and whether options are inherited or overridden.
 */
@Entity
@Table(
    name = "project_fields",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_project_field",
        columnNames = {"project_id", "field_id"}
    ),
    indexes = {
        @Index(name = "idx_project_fields_project_order", columnList = "project_id, is_enabled, sort_order"),
        @Index(name = "idx_project_fields_field",         columnList = "field_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private FieldDefinition field;

    /** Optional per-project label override. Falls back to FieldDefinition.name. */
    @Column(name = "display_name", length = 120)
    private String displayName;

    @Column(name = "help_text", length = 500)
    private String helpText;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled;

    @Column(name = "is_required", nullable = false)
    private boolean required;

    /**
     * When true, options come from field_options (global).
     * When false, options come from project_field_options (project-specific override).
     */
    @Column(name = "use_global_options", nullable = false)
    private boolean useGlobalOptions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    private void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = Instant.now();
    }
}
