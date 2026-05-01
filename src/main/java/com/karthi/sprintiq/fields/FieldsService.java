package com.karthi.sprintiq.fields;

import com.karthi.sprintiq.fields.dto.FieldDTO;
import com.karthi.sprintiq.fields.entity.Field;
import com.karthi.sprintiq.fields.repository.FieldRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FieldsService {

  private final FieldRepository fieldRepository;

  public List<FieldDTO> getAllFields() {
    return fieldRepository.findAll().stream().map(this::toDto).toList();
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
}
