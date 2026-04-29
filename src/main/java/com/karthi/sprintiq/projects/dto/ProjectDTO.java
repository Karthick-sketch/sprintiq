package com.karthi.sprintiq.projects.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDTO {

  private Long id;
  private String title;
  private String description;
  private Long ownerId;
  private List<Long> teamMemberIds;
}
