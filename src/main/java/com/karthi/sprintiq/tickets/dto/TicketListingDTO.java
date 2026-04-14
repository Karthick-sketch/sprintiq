package com.karthi.sprintiq.tickets.dto;

import com.karthi.sprintiq.tickets.enums.Priority;
import com.karthi.sprintiq.tickets.enums.Status;
import java.sql.Date;
import lombok.Data;

@Data
public class TicketListingDTO {

  private Long id;
  private String title;
  private Status status;
  private Priority priority;
  private Long assigneeId;
  private Date dueDate;
  private Long projectId;
}
