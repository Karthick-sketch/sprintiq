package com.karthi.sprintiq.tickets.dto;

import com.karthi.sprintiq.tickets.enums.TicketPriority;
import com.karthi.sprintiq.tickets.enums.TicketStatus;
import java.sql.Date;
import lombok.Data;

@Data
public class TicketCreateRequestDTO {

  private String title;
  private String description;
  private TicketStatus status;
  private TicketPriority priority;
  private Date dueDate;
  private Long assigneeId;
  private Long sectionId;
  private Integer orderIndex;
}
