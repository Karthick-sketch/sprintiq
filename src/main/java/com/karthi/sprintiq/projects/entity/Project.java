package com.karthi.sprintiq.projects.entity;

import com.karthi.sprintiq.tickets.entity.Ticket;
import com.karthi.sprintiq.user.entity.User;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  private String description;

  @ManyToOne
  private User owner;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<ProjectUser> teamMembers;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<Section> sections;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<Ticket> tickets;
}
