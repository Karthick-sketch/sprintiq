package com.karthi.sprintiq.fields.entity;

import com.karthi.sprintiq.user.entity.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.*;

/**
 * Normalized typed ticket field values.
 * One row per value item. Multi-value fields (MULTI_DROPDOWN, MULTI_USER)
 * use multiple rows with the same (ticket_id, project_field_id) and
 * increasing value_index values.
 *
 * Exactly one value column must be non-null per row.
 */
@Entity
@Table(
    name = "ticket_field_values",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_tfv_row",
        columnNames = {"ticket_id", "project_field_id", "value_index"}
    ),
    indexes = {
        @Index(name = "idx_tfv_ticket_field",   columnList = "ticket_id, project_field_id"),
        @Index(name = "idx_tfv_field_option",   columnList = "project_field_id, field_option_id"),
        @Index(name = "idx_tfv_project_option", columnList = "project_field_id, project_field_option_id"),
        @Index(name = "idx_tfv_user",           columnList = "project_field_id, user_id"),
        @Index(name = "idx_tfv_number",         columnList = "project_field_id, number_value"),
        @Index(name = "idx_tfv_date",           columnList = "project_field_id, date_value"),
        @Index(name = "idx_tfv_datetime",       columnList = "project_field_id, datetime_value"),
        @Index(name = "idx_tfv_boolean",        columnList = "project_field_id, boolean_value")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketFieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_field_id", nullable = false)
    private ProjectField projectField;

    /** For single-value fields this is always 0. Multi-value fields use 0, 1, 2, ... */
    @Column(name = "value_index", nullable = false)
    private Integer valueIndex;

    // ── Typed value columns ──────────────────────────────────────────────────
    // Exactly one must be non-null per row.

    @Column(name = "text_value", length = 2000)
    private String textValue;

    @Column(name = "number_value", precision = 19, scale = 4)
    private BigDecimal numberValue;

    @Column(name = "date_value")
    private LocalDate dateValue;

    @Column(name = "datetime_value")
    private Instant datetimeValue;

    @Column(name = "boolean_value")
    private Boolean booleanValue;

    /** Global field option (when project_fields.use_global_options = TRUE). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_option_id")
    private FieldOption fieldOption;

    /** Project-specific option (when project_fields.use_global_options = FALSE). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_field_option_id")
    private ProjectFieldOption projectFieldOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    private void prePersist() {
        if (valueIndex == null) valueIndex = 0;
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = Instant.now();
    }
}
