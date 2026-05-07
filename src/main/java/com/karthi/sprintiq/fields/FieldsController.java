package com.karthi.sprintiq.fields;

import com.karthi.sprintiq.fields.dto.FieldDTO;
import com.karthi.sprintiq.fields.dto.FieldOptionDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
public class FieldsController {

  private final FieldsService fieldsService;

  @GetMapping
  public List<FieldDTO> getAllFields() {
    return fieldsService.getAllFields();
  }

  @GetMapping("/options")
  public List<FieldOptionDTO> getFieldOptions() {
    return fieldsService.getAllFieldOptions();
  }
}
