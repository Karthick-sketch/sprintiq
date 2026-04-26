package com.karthi.sprintiq.tickets.dto;

import com.karthi.sprintiq.tickets.enums.Priority;
import com.karthi.sprintiq.tickets.enums.Status;
import java.sql.Date;
import lombok.Data;

@Data
public class TicketRequestDTO {

  private String title;
  private String description;
  private Status status;
  private Priority priority;
  private Date dueDate;
  private Long assigneeId;
  private Long sectionId;
  private Integer orderIndex;
}
