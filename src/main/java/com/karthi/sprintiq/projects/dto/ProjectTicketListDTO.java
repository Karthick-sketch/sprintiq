package com.karthi.sprintiq.projects.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectTicketListDTO {

  private Long id;
  private String title;
}
