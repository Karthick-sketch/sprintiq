package com.karthi.sprintiq.projects.dto;

import com.karthi.sprintiq.tickets.enums.Priority;
import com.karthi.sprintiq.tickets.enums.Status;
import lombok.Data;

@Data
public class ProjectSectionTicketDTO {

  private Long id;
  private String title;
  private Status status;
  private Priority priority;
  private Long assigneeId;
  private Long sectionId;
}
