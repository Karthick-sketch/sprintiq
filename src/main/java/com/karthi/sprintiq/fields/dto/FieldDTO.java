package com.karthi.sprintiq.fields.dto;

import com.karthi.sprintiq.fields.enums.FieldKind;
import com.karthi.sprintiq.fields.enums.FieldType;
import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldDTO {

  private Long id;
  private String systemKey;
  private String name;
  private String description;
  private FieldKind fieldKind;
  private FieldType fieldType;
  private boolean system;
  private boolean enabled;
  private boolean required;
  private boolean locked;
  private boolean searchable;
  private boolean active;
  private List<FieldOptionDTO> options;
}
