package com.karthi.sprintiq.projects.dto;

import com.karthi.sprintiq.tickets.dto.ProjectSectionTicketDTO;
import java.util.List;
import lombok.Data;

@Data
public class SectionDTO {

  private Long id;
  private String name;
  private List<ProjectSectionTicketDTO> tickets;
}
