package com.karthi.sprintiq.fields.entity;

import com.karthi.sprintiq.fields.enums.WorkflowSemanticKey;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(
    name = "project_field_options",
    uniqueConstraints = @UniqueConstraint(name = "uk_project_option_value", columnNames = {"project_field_id", "value_key"}),
    indexes = {
        @Index(name = "idx_pfo_field_order", columnList = "project_field_id, sort_order"),
        @Index(name = "idx_pfo_semantic",    columnList = "workflow_semantic_key")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectFieldOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_field_id", nullable = false)
    private ProjectField projectField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_field_option_id")
    private FieldOption sourceFieldOption;

    @Column(nullable = false, length = 120)
    private String label;

    @Column(name = "value_key", nullable = false, length = 120)
    private String valueKey;

    @Column(length = 30)
    private String color;

    @Column(length = 80)
    private String icon;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "workflow_semantic_key", length = 30)
    private WorkflowSemanticKey workflowSemanticKey;

    @Column(name = "is_default", nullable = false)
    private boolean defaultOption;

    @Column(name = "is_active", nullable = false)
    private boolean active;

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
