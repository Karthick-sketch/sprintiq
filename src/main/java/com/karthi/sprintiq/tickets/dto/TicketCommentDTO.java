package com.karthi.sprintiq.tickets.dto;

import java.time.LocalDate;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketCommentDTO {

  private Long id;
  private String content;
  private LocalDate createdAt;
  private Long createdById;
}
