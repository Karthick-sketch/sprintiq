package com.karthi.sprintiq.tickets.entity;

import com.karthi.sprintiq.customfields.entity.CustomField;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_custom_fields")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketCustomField {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Ticket ticket;

  @ManyToOne
  private CustomField customField;

  private String value;
}
