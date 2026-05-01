package com.karthi.sprintiq.tickets.dto;

import lombok.Data;

@Data
public class TicketCreateRequestDTO {

  private String title;
  private String description;
  private Long projectId;
  private Long sectionId;
  private Integer orderIndex;
}
