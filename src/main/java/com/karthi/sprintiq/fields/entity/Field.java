package com.karthi.sprintiq.fields.entity;

import com.karthi.sprintiq.fields.enums.FieldKind;
import com.karthi.sprintiq.fields.enums.FieldType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fields")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Field {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String key;

  @Column(nullable = false)
  private String name;

  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FieldKind kind;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FieldType type;

  private Boolean enabled;
  private Boolean required;
  private String defaultValue;
}
