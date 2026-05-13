package com.karthi.sprintiq.fields.entity;

import com.karthi.sprintiq.fields.enums.FieldKind;
import com.karthi.sprintiq.fields.enums.FieldType;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import lombok.*;

/**
 * Global field definition — the reusable metadata catalog.
 * Standard fields (is_system=true) are seeded and product-owned. Custom fields are admin-created.
 */
@Entity
@Table(
    name = "fields",
    indexes = {
      @Index(name = "idx_fields_kind_active", columnList = "field_kind, is_active"),
      @Index(name = "idx_fields_type", columnList = "field_type")
    })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Field {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Stable identifier for standard fields (e.g. "status", "priority"). Null for custom fields. */
  @Column(name = "system_key", unique = true, length = 80)
  private String systemKey;

  @Column(nullable = false, length = 120)
  private String name;

  @Column(length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "field_kind", nullable = false, length = 20)
  private FieldKind fieldKind;

  /** Technical storage/rendering type — this IS an enum because it is not user-configurable. */
  @Enumerated(EnumType.STRING)
  @Column(name = "field_type", nullable = false, length = 30)
  private FieldType fieldType;

  /** True for seeded standard fields that the product understands. */
  @Column(name = "is_system", nullable = false)
  private boolean system;

  /** True prevents type changes and other destructive edits. */
  @Column(name = "is_locked", nullable = false)
  private boolean locked;

  @Column(name = "is_searchable", nullable = false)
  @Builder.Default
  private boolean searchable = true;

  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private boolean active = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  /** Global default options — do NOT eagerly fetch; use repository queries per field. */
  @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("sortOrder ASC")
  private List<FieldOption> options;

  @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<FieldTemplateItem> templateItems;

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
