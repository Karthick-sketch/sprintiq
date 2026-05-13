package com.karthi.sprintiq.fields.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Entity
@Table(
    name = "field_templates",
    uniqueConstraints =
        @UniqueConstraint(name = "uk_field_templates_key", columnNames = "template_key"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldTemplate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120)
  private String name;

  @Column(length = 500)
  private String description;

  @Column(name = "template_key", nullable = false, length = 80)
  private String templateKey;

  @Column(name = "is_default", nullable = false)
  private boolean defaultTemplate;

  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private boolean active = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("sortOrder ASC")
  private List<FieldTemplateItem> templateItems;

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
