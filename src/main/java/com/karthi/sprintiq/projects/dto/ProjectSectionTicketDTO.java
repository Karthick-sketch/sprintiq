package com.karthi.sprintiq.projects.dto;

import com.karthi.sprintiq.tickets.enums.Priority;
import com.karthi.sprintiq.tickets.enums.Status;
import com.karthi.sprintiq.user.dto.UserDTO;
import java.sql.Date;
import lombok.Data;

@Data
public class ProjectSectionTicketDTO {

  private Long id;
  private String title;
  private Status status;
  private Priority priority;
  private UserDTO assignee;
  private Date dueDate;
  private Long sectionId;
}
