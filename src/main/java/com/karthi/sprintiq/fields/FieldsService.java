package com.karthi.sprintiq.fields;

import com.karthi.sprintiq.fields.dto.FieldDTO;
import com.karthi.sprintiq.fields.dto.FieldOptionDTO;
import com.karthi.sprintiq.fields.entity.Field;
import com.karthi.sprintiq.fields.entity.FieldOption;
import com.karthi.sprintiq.fields.repository.FieldOptionRepository;
import com.karthi.sprintiq.fields.repository.FieldRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FieldsService {

  private final FieldRepository fieldRepository;
  private final FieldOptionRepository fieldOptionRepository;

  public List<FieldDTO> getAllFields() {
    return fieldRepository.findAll().stream().map(this::toDto).toList();
  }

  public List<FieldOptionDTO> getAllFieldOptions() {
    return toOptionsDTO(fieldOptionRepository.findAll());
  }

  private FieldDTO toDto(Field field) {
    return FieldDTO.builder()
      .id(field.getId())
      .key(field.getKey())
      .name(field.getName())
      .description(field.getDescription())
      .kind(field.getKind())
      .type(field.getType())
      .enabled(field.getEnabled())
      .required(field.getRequired())
      .defaultValue(field.getDefaultValue())
      .build();
  }

  private List<FieldOptionDTO> toOptionsDTO(List<FieldOption> options) {
    return options.stream().map(this::toOptionDTO).toList();
  }

  private FieldOptionDTO toOptionDTO(FieldOption option) {
    return FieldOptionDTO.builder()
      .id(option.getId())
      .fieldId(option.getField().getId())
      .value(option.getValue())
      .orderIndex(option.getOrderIndex())
      .colorNumber(option.getColorNumber())
      .build();
  }
}
