package com.karthi.sprintiq.customfields.dto;

import com.karthi.sprintiq.customfields.enums.FieldKind;
import com.karthi.sprintiq.customfields.enums.FieldType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomFieldDTO {

  private Long id;
  private String key;
  private String name;
  private String description;
  private FieldKind kind;
  private FieldType type;
  private Boolean enabled;
  private Boolean required;
  private String defaultValue;
}
