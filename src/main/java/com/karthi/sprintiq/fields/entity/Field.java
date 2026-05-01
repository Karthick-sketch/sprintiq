package com.karthi.sprintiq.fields.entity;

import com.karthi.sprintiq.fields.enums.FieldKind;
import com.karthi.sprintiq.fields.enums.FieldType;
import com.karthi.sprintiq.tickets.entity.TicketField;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

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

  @Column(nullable = false)
  private Boolean enabled;

  @Column(nullable = false)
  private Boolean required;

  @Column(name = "default_value")
  private String defaultValue;

  @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
  private List<FieldOption> options;

  @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
  private List<FieldTemplateItem> templateItems;

  @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
  private List<TicketField> ticketFields;
}
