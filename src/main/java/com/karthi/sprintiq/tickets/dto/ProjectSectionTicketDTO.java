package com.karthi.sprintiq.tickets.dto;

import lombok.Data;

@Data
public class ProjectSectionTicketDTO {

  private Long id;
  private String title;
  private String status;
  private String priority;
  private String assignee;
}
