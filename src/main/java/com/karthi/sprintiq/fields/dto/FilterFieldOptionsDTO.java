package com.karthi.sprintiq.fields.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterFieldOptionsDTO {
  private List<FilterFieldOptionDTO> status;
  private List<FilterFieldOptionDTO> priority;
}
