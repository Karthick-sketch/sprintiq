package com.karthi.sprintiq.tickets.dto;

import com.karthi.sprintiq.tickets.enums.Priority;
import com.karthi.sprintiq.tickets.enums.Status;
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
public class TicketDTO {

  private Long id;
  private String title;
  private String description;
  private Status status;
  private Priority priority;
  private Date dueDate;
  private UserDTO assignee;
  private Long sectionId;
  private Integer orderIndex;
}
