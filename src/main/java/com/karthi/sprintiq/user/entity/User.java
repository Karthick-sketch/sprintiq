package com.karthi.sprintiq.user.entity;

import com.karthi.sprintiq.projects.entity.Project;
import com.karthi.sprintiq.tickets.entity.Ticket;
import com.karthi.sprintiq.user.enums.Role;
import com.karthi.sprintiq.user.enums.UserStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserStatus status;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "owner")
  private List<Project> projects;

  @OneToMany(mappedBy = "assignee")
  private List<Ticket> tickets;
}
