package com.karthi.sprintiq.fields.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "field_templates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldTemplate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
  private List<FieldTemplateItem> templateItems;
}
