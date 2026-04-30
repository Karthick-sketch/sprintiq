package com.karthi.sprintiq.customfields;

import com.karthi.sprintiq.customfields.dto.CustomFieldDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
public class CustomFieldsController {

  private final CustomFieldsService customFieldsService;

  @GetMapping
  public List<CustomFieldDTO> getAllFields() {
    return customFieldsService.getAllFields();
  }
}
