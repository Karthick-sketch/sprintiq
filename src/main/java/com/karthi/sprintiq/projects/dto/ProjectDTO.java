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
public class ProjectDTO {

  private Long id;
  private String title;
  private String description;
  private Long ownerId;
  private List<Long> teamMemberIds;
}
