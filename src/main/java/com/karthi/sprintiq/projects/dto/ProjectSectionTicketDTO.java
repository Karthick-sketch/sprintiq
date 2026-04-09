package com.karthi.sprintiq.projects.dto;

import lombok.Data;

@Data
public class ProjectSectionTicketDTO {

  private Long id;
  private String title;
  private String status;
  private String priority;
  private Long assigneeId;
  private Long sectionId;
}
