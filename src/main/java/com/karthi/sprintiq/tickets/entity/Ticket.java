package com.karthi.sprintiq.tickets.entity;

import com.karthi.sprintiq.projects.entity.Project;
import com.karthi.sprintiq.projects.entity.Section;
import com.karthi.sprintiq.tickets.enums.TicketPriority;
import com.karthi.sprintiq.tickets.enums.TicketStatus;
import com.karthi.sprintiq.user.entity.User;
import jakarta.persistence.*;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TicketStatus status;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TicketPriority priority;

  private Date dueDate;

  @ManyToOne
  @JoinColumn(name = "assignee_id")
  private User assignee;

  @ManyToOne
  @JoinColumn(name = "reporter_id")
  private User reporter;

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @ManyToOne
  @JoinColumn(name = "section_id")
  private Section section;

  @Column(name = "order_index", nullable = false)
  private Integer orderIndex;
}
