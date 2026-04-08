package com.karthi.sprintiq.projects.dto;

import java.util.List;
import lombok.Data;

@Data
public class ProjectDTO {

  private Long id;
  private String name;
  private String description;
  private Long ownerId;
  private List<Long> teamMemberIds;
}
