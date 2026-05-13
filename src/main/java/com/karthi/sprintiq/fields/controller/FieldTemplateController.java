package com.karthi.sprintiq.fields.controller;

import com.karthi.sprintiq.fields.service.FieldTemplateService;
import com.karthi.sprintiq.fields.entity.FieldTemplate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/field-templates")
@RequiredArgsConstructor
public class FieldTemplateController {

  private final FieldTemplateService fieldTemplateService;

  @GetMapping
  public List<FieldTemplate> getAllTemplates() {
    return fieldTemplateService.getAllTemplates();
  }

  @PostMapping("/{templateId}/apply/{projectId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void applyTemplate(@PathVariable Long templateId, @PathVariable Long projectId) {
    fieldTemplateService.applyTemplate(projectId, templateId);
  }

  @PostMapping("/apply-default/{projectId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void applyDefaultTemplate(@PathVariable Long projectId) {
    fieldTemplateService.applyDefaultTemplate(projectId);
  }
}
