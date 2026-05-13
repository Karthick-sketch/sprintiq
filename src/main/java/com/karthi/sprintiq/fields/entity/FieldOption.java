package com.karthi.sprintiq.fields.entity;

import com.karthi.sprintiq.fields.enums.WorkflowSemanticKey;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

/**
 * Global default options for dropdown, radio, status, priority, etc.
 * value_key is the stable API identity (e.g. "high", "in_progress").
 * label can change without breaking saved ticket values.
 */
@Entity
@Table(
    name = "field_options",
    uniqueConstraints = @UniqueConstraint(name = "uk_field_options_value", columnNames = {"field_id", "value_key"}),
    indexes = {
        @Index(name = "idx_field_options_field_order", columnList = "field_id, sort_order"),
        @Index(name = "idx_field_options_semantic",    columnList = "workflow_semantic_key")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @Column(nullable = false, length = 120)
    private String label;

    /** Stable API key — never rename this after data exists. */
    @Column(name = "value_key", nullable = false, length = 120)
    private String valueKey;

    @Column(length = 30)
    private String color;

    @Column(length = 80)
    private String icon;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    /**
     * Cross-project workflow semantic. Only for status-like options.
     * Drives burndown, WIP, done-state detection, and cross-project search.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "workflow_semantic_key", length = 30)
    private WorkflowSemanticKey workflowSemanticKey;

    @Column(name = "is_default", nullable = false)
    private boolean defaultOption;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    private void prePersist() {
        active = true;
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = Instant.now();
    }
}
