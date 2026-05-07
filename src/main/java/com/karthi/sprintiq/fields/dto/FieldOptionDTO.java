package com.karthi.sprintiq.fields.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldOptionDTO {

  private Long id;
  private Long fieldId;
  private String value;
  private Integer orderIndex;
  private Integer colorNumber;
}
