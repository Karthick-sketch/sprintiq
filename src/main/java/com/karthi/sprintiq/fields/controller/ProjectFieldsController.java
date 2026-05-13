package com.karthi.sprintiq.fields.controller;

import com.karthi.sprintiq.fields.service.ProjectFieldService;
import com.karthi.sprintiq.fields.dto.FieldOptionDTO;
import com.karthi.sprintiq.fields.dto.ProjectFieldRequest;
import com.karthi.sprintiq.fields.dto.ProjectFieldResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/fields")
@RequiredArgsConstructor
public class ProjectFieldsController {

    private final ProjectFieldService projectFieldService;

    @GetMapping
    public List<ProjectFieldResponse> getProjectFields(@PathVariable Long projectId) {
        return projectFieldService.getProjectFields(projectId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectFieldResponse assignField(
        @PathVariable Long projectId,
        @RequestBody ProjectFieldRequest request
    ) {
        return projectFieldService.assignField(projectId, request);
    }

    @PutMapping("/reorder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reorderProjectFields(
        @PathVariable Long projectId,
        @RequestBody List<Long> orderedIds
    ) {
        projectFieldService.reorderProjectFields(projectId, orderedIds);
    }

    @PatchMapping("/{projectFieldId}")
    public ProjectFieldResponse updateProjectField(
        @PathVariable Long projectId,
        @PathVariable Long projectFieldId,
        @RequestBody ProjectFieldRequest request
    ) {
        return projectFieldService.updateProjectField(projectFieldId, request);
    }

    @PostMapping("/{projectFieldId}/options")
    @ResponseStatus(HttpStatus.CREATED)
    public FieldOptionDTO addProjectFieldOption(
        @PathVariable Long projectId,
        @PathVariable Long projectFieldId,
        @RequestBody FieldOptionDTO request
    ) {
        return projectFieldService.addProjectFieldOption(projectFieldId, request);
    }

    @PostMapping("/{projectFieldId}/options/copy-global")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void copyGlobalOptions(
        @PathVariable Long projectId,
        @PathVariable Long projectFieldId
    ) {
        projectFieldService.copyGlobalOptionsToProject(projectFieldId);
    }
}
