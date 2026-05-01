package com.karthi.sprintiq.fields.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "field_template_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldTemplateItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "template_id", nullable = false)
  private FieldTemplate template;

  @ManyToOne
  @JoinColumn(name = "field_id", nullable = false)
  private Field field;

  @Column(nullable = false)
  private Boolean enabled;

  @Column(nullable = false)
  private Boolean required;

  @Column(name = "order_index", nullable = false)
  private Integer orderIndex;
}
