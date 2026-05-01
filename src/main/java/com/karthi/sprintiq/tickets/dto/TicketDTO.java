package com.karthi.sprintiq.tickets.dto;

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
  private Long projectId;
  private Long sectionId;
  private Integer orderIndex;
}
