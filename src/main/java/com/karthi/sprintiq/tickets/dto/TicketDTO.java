package com.karthi.sprintiq.tickets.dto;

import java.sql.Date;
import lombok.Data;

@Data
public class TicketDTO {

  private Long id;
  private String title;
  private String description;
  private String status;
  private String priority;
  private Date dueDate;
  private Long assigneeId;
  private Long sectionId;
}
