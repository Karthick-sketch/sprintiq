package com.karthi.sprintiq.fields.dto;

import com.karthi.sprintiq.fields.enums.FieldKind;
import com.karthi.sprintiq.fields.enums.FieldType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldDTO {

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
