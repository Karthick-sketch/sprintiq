package com.karthi.sprintiq.projects.dto;

import java.util.List;
import lombok.Data;

@Data
public class SectionDTO {

  private Long id;
  private String name;
  private List<ProjectSectionTicketDTO> tickets;
}
