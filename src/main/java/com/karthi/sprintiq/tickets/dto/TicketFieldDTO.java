package com.karthi.sprintiq.tickets.dto;

import com.karthi.sprintiq.fields.dto.FieldDTO;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketFieldDTO {

  private Long id;
  private FieldDTO field;
  private String value;
}
