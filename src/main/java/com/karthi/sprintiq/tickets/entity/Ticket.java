package com.karthi.sprintiq.tickets.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karthi.sprintiq.projects.entity.Project;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private String status;
  private String priority;
  private String assignee;
  private Date dueDate;

  @JsonIgnore
  @ManyToOne(optional = false)
  @JoinColumn(name = "project_id")
  private Project project;
}
