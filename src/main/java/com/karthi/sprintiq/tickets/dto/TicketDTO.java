package com.karthi.sprintiq.tickets.dto;

import com.karthi.sprintiq.tickets.enums.Priority;
import com.karthi.sprintiq.tickets.enums.Status;
import java.sql.Date;
import lombok.Data;

@Data
public class TicketDTO {

  private Long id;
  private String title;
  private String description;
  private Status status;
  private Priority priority;
  private Date dueDate;
  private Long assigneeId;
  private Long sectionId;
}
