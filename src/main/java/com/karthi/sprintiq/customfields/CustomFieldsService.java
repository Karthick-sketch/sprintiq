package com.karthi.sprintiq.customfields;

import com.karthi.sprintiq.customfields.dto.CustomFieldDTO;
import com.karthi.sprintiq.customfields.entity.CustomField;
import com.karthi.sprintiq.customfields.repository.CustomFieldRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomFieldsService {

  private final CustomFieldRepository fieldRepository;

  public List<CustomFieldDTO> getAllFields() {
    return fieldRepository.findAll().stream().map(this::toDto).toList();
  }

  private CustomFieldDTO toDto(CustomField field) {
    return CustomFieldDTO.builder()
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
