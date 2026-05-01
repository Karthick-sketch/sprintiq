package com.karthi.sprintiq.fields.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "field_options")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "field_id", nullable = false)
  private Field field;

  @Column(nullable = false)
  private String value;

  @Column(name = "order_index", nullable = false)
  private Integer orderIndex;
}
