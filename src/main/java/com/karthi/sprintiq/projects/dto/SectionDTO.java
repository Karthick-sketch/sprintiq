package com.karthi.sprintiq.projects.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionDTO {

  private Long id;
  private String title;
  private Long projectId;
  private List<ProjectSectionTicketDTO> tickets;
  private Integer orderIndex;
}
