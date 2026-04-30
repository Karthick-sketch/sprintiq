package com.karthi.sprintiq.tickets.dto;

import com.karthi.sprintiq.projects.dto.ProjectTicketListDTO;
import com.karthi.sprintiq.tickets.enums.TicketPriority;
import com.karthi.sprintiq.tickets.enums.TicketStatus;
import com.karthi.sprintiq.user.dto.UserDTO;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketListingDTO {

  private Long id;
  private String title;
  private TicketStatus status;
  private TicketPriority priority;
  private Date dueDate;
  private UserDTO assignee;
  private UserDTO reporter;
  private ProjectTicketListDTO project;
}
