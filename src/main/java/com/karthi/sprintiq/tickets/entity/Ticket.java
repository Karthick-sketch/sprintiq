package com.karthi.sprintiq.tickets.entity;

import com.karthi.sprintiq.projects.entity.Section;
import com.karthi.sprintiq.tickets.enums.Priority;
import com.karthi.sprintiq.tickets.enums.Status;
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
  private Status status;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Priority priority;

  private Date dueDate;

  @ManyToOne(optional = false)
  @JoinColumn(name = "assignee_id")
  private User assignee;

  @ManyToOne(optional = false)
  @JoinColumn(name = "section_id")
  private Section section;

  private Integer orderIndex;
}
