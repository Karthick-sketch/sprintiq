package com.karthi.sprintiq.fields;

import com.karthi.sprintiq.fields.dto.FieldDTO;
import com.karthi.sprintiq.fields.dto.FieldOptionDTO;
import com.karthi.sprintiq.fields.enums.FieldKind;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
public class FieldsController {

    private final FieldsService fieldsService;

    @GetMapping
    public List<FieldDTO> getAllFields(
        @RequestParam(required = false) FieldKind kind,
        @RequestParam(required = false) Boolean active
    ) {
        return fieldsService.getAllFields(kind, active);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FieldDTO createField(@RequestBody FieldDTO request) {
        return fieldsService.createField(request);
    }

    @PatchMapping("/{fieldId}")
    public FieldDTO updateField(@PathVariable Long fieldId, @RequestBody FieldDTO request) {
        return fieldsService.updateField(fieldId, request);
    }

    @DeleteMapping("/{fieldId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateField(@PathVariable Long fieldId) {
        fieldsService.deactivateField(fieldId);
    }

    @PostMapping("/{fieldId}/options")
    @ResponseStatus(HttpStatus.CREATED)
    public FieldOptionDTO addOption(
        @PathVariable Long fieldId,
        @RequestBody FieldOptionDTO request
    ) {
        return fieldsService.addOption(fieldId, request);
    }

    @PatchMapping("/{fieldId}/options/{optionId}")
    public FieldOptionDTO updateOption(
        @PathVariable Long fieldId,
        @PathVariable Long optionId,
        @RequestBody FieldOptionDTO request
    ) {
        return fieldsService.updateOption(fieldId, optionId, request);
    }

    @PutMapping("/{fieldId}/options/reorder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reorderOptions(
        @PathVariable Long fieldId,
        @RequestBody List<Long> orderedIds
    ) {
        fieldsService.reorderOptions(fieldId, orderedIds);
    }
}
