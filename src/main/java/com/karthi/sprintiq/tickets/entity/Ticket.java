package com.karthi.sprintiq.tickets.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.karthi.sprintiq.projects.entity.Project;
import com.karthi.sprintiq.projects.entity.Section;
import com.karthi.sprintiq.user.entity.User;
import jakarta.persistence.*;
import java.util.List;
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

  @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<TicketComment> comments;

  @ManyToOne
  @JoinColumn(name = "created_by")
  private User createdBy;

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @ManyToOne
  @JoinColumn(name = "section_id")
  private Section section;

  @Column(name = "order_index", nullable = false)
  private Integer orderIndex;

  @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<TicketField> ticketFields;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Ticket parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private List<Ticket> subTickets;
}
