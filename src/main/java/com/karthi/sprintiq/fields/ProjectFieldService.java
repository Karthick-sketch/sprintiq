package com.karthi.sprintiq.fields;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.fields.dto.FieldOptionDTO;
import com.karthi.sprintiq.fields.dto.ProjectFieldRequest;
import com.karthi.sprintiq.fields.dto.ProjectFieldResponse;
import com.karthi.sprintiq.fields.entity.FieldDefinition;
import com.karthi.sprintiq.fields.entity.FieldOption;
import com.karthi.sprintiq.fields.entity.ProjectField;
import com.karthi.sprintiq.fields.entity.ProjectFieldOption;
import com.karthi.sprintiq.fields.repository.FieldOptionRepository;
import com.karthi.sprintiq.fields.repository.ProjectFieldOptionRepository;
import com.karthi.sprintiq.fields.repository.ProjectFieldRepository;
import com.karthi.sprintiq.projects.ProjectsService;
import com.karthi.sprintiq.projects.entity.Project;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectFieldService {

    private final ProjectFieldRepository projectFieldRepository;
    private final ProjectFieldOptionRepository projectFieldOptionRepository;
    private final FieldOptionRepository fieldOptionRepository;
    private final FieldsService fieldsService;
    private final ProjectsService projectsService;

    // ── Project Field Queries ────────────────────────────────────────────────

    public List<ProjectFieldResponse> getProjectFields(Long projectId) {
        return projectFieldRepository.findByProjectIdOrderBySortOrderAsc(projectId)
            .stream().map(this::toResponse).toList();
    }

    public List<ProjectFieldResponse> getEnabledProjectFields(Long projectId) {
        return projectFieldRepository.findByProjectIdAndEnabledTrueOrderBySortOrderAsc(projectId)
            .stream().map(this::toResponse).toList();
    }

    // ── Assign / Update / Reorder ────────────────────────────────────────────

    @Transactional
    public ProjectFieldResponse assignField(Long projectId, ProjectFieldRequest request) {
        if (projectFieldRepository.existsByProjectIdAndFieldId(projectId, request.getFieldId())) {
            throw new IllegalStateException("Field " + request.getFieldId() + " is already assigned to project " + projectId);
        }
        Project project = projectsService.getProjectById(projectId);
        FieldDefinition field = fieldsService.getFieldById(request.getFieldId());

        ProjectField pf = ProjectField.builder()
            .project(project)
            .field(field)
            .displayName(request.getDisplayName())
            .helpText(request.getHelpText())
            .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
            .enabled(request.isEnabled())
            .required(request.isRequired())
            .useGlobalOptions(request.isUseGlobalOptions())
            .build();
        return toResponse(projectFieldRepository.save(pf));
    }

    @Transactional
    public ProjectFieldResponse updateProjectField(Long projectFieldId, ProjectFieldRequest request) {
        ProjectField pf = getProjectFieldById(projectFieldId);
        pf.setDisplayName(request.getDisplayName());
        pf.setHelpText(request.getHelpText());
        pf.setSortOrder(request.getSortOrder());
        pf.setEnabled(request.isEnabled());
        pf.setRequired(request.isRequired());
        pf.setUseGlobalOptions(request.isUseGlobalOptions());
        return toResponse(projectFieldRepository.save(pf));
    }

    @Transactional
    public void reorderProjectFields(Long projectId, List<Long> orderedIds) {
        List<ProjectField> fields = projectFieldRepository.findByProjectIdOrderBySortOrderAsc(projectId);
        for (int i = 0; i < orderedIds.size(); i++) {
            Long pfId = orderedIds.get(i);
            int idx = i;
            fields.stream()
                .filter(pf -> pf.getId().equals(pfId))
                .findFirst()
                .ifPresent(pf -> pf.setSortOrder(idx * 10));
        }
        projectFieldRepository.saveAll(fields);
    }

    // ── Project-Specific Option Management ──────────────────────────────────

    @Transactional
    public void copyGlobalOptionsToProject(Long projectFieldId) {
        ProjectField pf = getProjectFieldById(projectFieldId);
        List<FieldOption> globalOptions = fieldOptionRepository
            .findByFieldIdOrderBySortOrderAsc(pf.getField().getId());

        List<ProjectFieldOption> overrides = globalOptions.stream().map(go ->
            ProjectFieldOption.builder()
                .projectField(pf)
                .sourceFieldOption(go)
                .label(go.getLabel())
                .valueKey(go.getValueKey())
                .color(go.getColor())
                .icon(go.getIcon())
                .sortOrder(go.getSortOrder())
                .workflowSemanticKey(go.getWorkflowSemanticKey())
                .defaultOption(go.isDefaultOption())
                .active(true)
                .build()
        ).toList();
        projectFieldOptionRepository.saveAll(overrides);

        pf.setUseGlobalOptions(false);
        projectFieldRepository.save(pf);
    }

    public FieldOptionDTO addProjectFieldOption(Long projectFieldId, FieldOptionDTO request) {
        ProjectField pf = getProjectFieldById(projectFieldId);
        ProjectFieldOption opt = ProjectFieldOption.builder()
            .projectField(pf)
            .label(request.getLabel())
            .valueKey(request.getValueKey())
            .color(request.getColor())
            .icon(request.getIcon())
            .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
            .workflowSemanticKey(request.getWorkflowSemanticKey())
            .defaultOption(request.isDefaultOption())
            .active(true)
            .build();
        ProjectFieldOption saved = projectFieldOptionRepository.save(opt);
        return toOptionDto(saved);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    public ProjectField getProjectFieldById(Long id) {
        return projectFieldRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ProjectField not found: " + id));
    }

    private ProjectFieldResponse toResponse(ProjectField pf) {
        List<FieldOptionDTO> options;
        if (pf.isUseGlobalOptions()) {
            options = fieldOptionRepository
                .findByFieldIdAndActiveTrue(pf.getField().getId())
                .stream().map(fieldsService::toOptionDto).toList();
        } else {
            options = projectFieldOptionRepository
                .findByProjectFieldIdAndActiveTrue(pf.getId())
                .stream().map(this::toOptionDto).toList();
        }

        String resolvedName = pf.getDisplayName() != null
            ? pf.getDisplayName() : pf.getField().getName();

        return ProjectFieldResponse.builder()
            .projectFieldId(pf.getId())
            .fieldId(pf.getField().getId())
            .systemKey(pf.getField().getSystemKey())
            .name(pf.getField().getName())
            .displayName(resolvedName)
            .helpText(pf.getHelpText())
            .fieldType(pf.getField().getFieldType())
            .sortOrder(pf.getSortOrder())
            .enabled(pf.isEnabled())
            .required(pf.isRequired())
            .useGlobalOptions(pf.isUseGlobalOptions())
            .options(options)
            .build();
    }

    private FieldOptionDTO toOptionDto(ProjectFieldOption opt) {
        return FieldOptionDTO.builder()
            .id(opt.getId())
            .label(opt.getLabel())
            .valueKey(opt.getValueKey())
            .color(opt.getColor())
            .icon(opt.getIcon())
            .sortOrder(opt.getSortOrder())
            .workflowSemanticKey(opt.getWorkflowSemanticKey())
            .defaultOption(opt.isDefaultOption())
            .active(opt.isActive())
            .build();
    }
}
