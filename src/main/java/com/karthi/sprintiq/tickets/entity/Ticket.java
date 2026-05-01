package com.karthi.sprintiq.tickets.entity;

import com.karthi.sprintiq.projects.entity.Project;
import com.karthi.sprintiq.projects.entity.Section;
import jakarta.persistence.*;
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

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @ManyToOne
  @JoinColumn(name = "section_id")
  private Section section;

  @Column(name = "order_index", nullable = false)
  private Integer orderIndex;
}
