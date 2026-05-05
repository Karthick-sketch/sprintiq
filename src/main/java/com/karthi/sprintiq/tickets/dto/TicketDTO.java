package com.karthi.sprintiq.tickets.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {

  private Long id;
  private String title;
  private String description;
  private List<TicketCommentDTO> comments;
  private List<TicketFieldDTO> ticketFields;
  private List<Long> subTicketIds;
  private Long parentId;
  private Long projectId;
  private Long sectionId;
  private Integer orderIndex;
}
