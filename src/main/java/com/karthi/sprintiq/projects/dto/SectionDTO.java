package com.karthi.sprintiq.projects.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectionDTO {

  private Long id;
  private String name;
  private Long projectId;
  private List<ProjectSectionTicketDTO> tickets;
}
