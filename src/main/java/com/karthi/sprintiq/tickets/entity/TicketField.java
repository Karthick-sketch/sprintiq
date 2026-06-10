package com.karthi.sprintiq.tickets.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.karthi.sprintiq.fields.entity.Field;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket_fields")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketField {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ticket_id", nullable = false)
  @JsonBackReference
  private Ticket ticket;

  @ManyToOne
  @JoinColumn(name = "field_id", nullable = false)
  private Field field;

  @Column(nullable = false)
  private String value;
}
