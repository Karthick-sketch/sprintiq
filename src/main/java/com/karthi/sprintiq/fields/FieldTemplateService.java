package com.karthi.sprintiq.fields;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.fields.entity.FieldDefinition;
import com.karthi.sprintiq.fields.entity.FieldTemplate;
import com.karthi.sprintiq.fields.entity.FieldTemplateItem;
import com.karthi.sprintiq.fields.entity.ProjectField;
import com.karthi.sprintiq.fields.repository.FieldTemplateRepository;
import com.karthi.sprintiq.fields.repository.ProjectFieldRepository;
import com.karthi.sprintiq.projects.ProjectsService;
import com.karthi.sprintiq.projects.entity.Project;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FieldTemplateService {

    private final FieldTemplateRepository fieldTemplateRepository;
    private final ProjectFieldRepository projectFieldRepository;
    private final ProjectsService projectsService;

    public List<FieldTemplate> getAllTemplates() {
        return fieldTemplateRepository.findByActiveTrue();
    }

    public FieldTemplate getById(Long id) {
        return fieldTemplateRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Template not found: " + id));
    }

    /**
     * Idempotent template application.
     * Upserts project_fields for each template item — never duplicates.
     */
    @Transactional
    public void applyTemplate(Long projectId, Long templateId) {
        Project project = projectsService.getProjectById(projectId);
        FieldTemplate template = getById(templateId);

        List<FieldTemplateItem> items = template.getTemplateItems();
        if (items == null || items.isEmpty()) return;

        for (FieldTemplateItem item : items) {
            FieldDefinition field = item.getField();
            // Idempotent: skip if already assigned
            if (projectFieldRepository.existsByProjectIdAndFieldId(projectId, field.getId())) {
                continue;
            }
            ProjectField pf = ProjectField.builder()
                .project(project)
                .field(field)
                .sortOrder(item.getSortOrder())
                .enabled(item.isEnabled())
                .required(item.isRequired())
                .useGlobalOptions(true)
                .build();
            projectFieldRepository.save(pf);
        }
    }

    @Transactional
    public void applyDefaultTemplate(Long projectId) {
        FieldTemplate defaultTemplate = fieldTemplateRepository.findByDefaultTemplateTrue()
            .orElseThrow(() -> new EntityNotFoundException("No default field template found."));
        applyTemplate(projectId, defaultTemplate.getId());
    }
}
