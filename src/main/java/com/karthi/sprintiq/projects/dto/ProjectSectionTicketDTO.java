package com.karthi.sprintiq.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSectionTicketDTO {

  private Long id;
  private String title;
  private Long projectId;
  private Long sectionId;
  private Integer orderIndex;
}
