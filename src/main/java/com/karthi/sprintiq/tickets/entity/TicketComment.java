package com.karthi.sprintiq.tickets.entity;

import com.karthi.sprintiq.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "ticket_comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private LocalDate createdAt;

  @ManyToOne
  @JoinColumn(name = "ticket_id", nullable = false)
  private Ticket ticket;

  @ManyToOne
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;
}
