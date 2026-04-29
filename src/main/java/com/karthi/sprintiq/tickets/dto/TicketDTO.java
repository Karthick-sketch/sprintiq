package com.karthi.sprintiq.tickets.dto;

import com.karthi.sprintiq.tickets.enums.TicketPriority;
import com.karthi.sprintiq.tickets.enums.TicketStatus;
import com.karthi.sprintiq.user.dto.UserDTO;
import java.sql.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketDTO {

  private Long id;
  private String title;
  private String description;
  private TicketStatus status;
  private TicketPriority priority;
  private Date dueDate;
  private UserDTO assignee;
  private Long sectionId;
  private Integer orderIndex;
}
