package com.karthi.sprintiq.fields;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.fields.dto.FieldDTO;
import com.karthi.sprintiq.fields.dto.FieldOptionDTO;
import com.karthi.sprintiq.fields.entity.FieldDefinition;
import com.karthi.sprintiq.fields.entity.FieldOption;
import com.karthi.sprintiq.fields.enums.FieldKind;
import com.karthi.sprintiq.fields.repository.FieldOptionRepository;
import com.karthi.sprintiq.fields.repository.FieldRepository;
import com.karthi.sprintiq.fields.repository.TicketFieldValueRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FieldsService {

    private final FieldRepository fieldRepository;
    private final FieldOptionRepository fieldOptionRepository;
    private final TicketFieldValueRepository ticketFieldValueRepository;

    // ── Field Definition CRUD ────────────────────────────────────────────────

    public List<FieldDTO> getAllFields(FieldKind kind, Boolean active) {
        List<FieldDefinition> fields;
        if (kind != null && active != null && active) {
            fields = fieldRepository.findByFieldKindAndActiveTrue(kind);
        } else if (kind != null) {
            fields = fieldRepository.findAll().stream()
                .filter(f -> f.getFieldKind() == kind).toList();
        } else if (active != null && active) {
            fields = fieldRepository.findByActiveTrue();
        } else {
            fields = fieldRepository.findAll();
        }
        return fields.stream().map(this::toDto).toList();
    }

    public FieldDTO createField(FieldDTO request) {
        if (request.getFieldKind() == FieldKind.STANDARD) {
            throw new IllegalArgumentException("Standard fields cannot be created via API. They are system-seeded.");
        }
        FieldDefinition field = FieldDefinition.builder()
            .name(request.getName())
            .description(request.getDescription())
            .fieldKind(FieldKind.CUSTOM)
            .fieldType(request.getFieldType())
            .system(false)
            .locked(false)
            .searchable(request.isSearchable())
            .active(true)
            .build();
        return toDto(fieldRepository.save(field));
    }

    @Transactional
    public FieldDTO updateField(Long id, FieldDTO request) {
        FieldDefinition field = getFieldById(id);
        if (field.isLocked()) {
            throw new IllegalStateException("Field '" + field.getName() + "' is locked and cannot be modified.");
        }
        // Block field type change if values exist
        if (field.getFieldType() != request.getFieldType() &&
            ticketFieldValueRepository.existsByProjectFieldId(id)) {
            throw new IllegalStateException("Cannot change field type when ticket values already exist.");
        }
        field.setName(request.getName());
        field.setDescription(request.getDescription());
        field.setFieldType(request.getFieldType());
        field.setSearchable(request.isSearchable());
        return toDto(fieldRepository.save(field));
    }

    public void deactivateField(Long id) {
        FieldDefinition field = getFieldById(id);
        if (field.isSystem()) {
            throw new IllegalStateException("System fields cannot be deactivated.");
        }
        field.setActive(false);
        fieldRepository.save(field);
    }

    // ── Global Option Management ─────────────────────────────────────────────

    public FieldOptionDTO addOption(Long fieldId, FieldOptionDTO request) {
        FieldDefinition field = getFieldById(fieldId);
        FieldOption option = FieldOption.builder()
            .field(field)
            .label(request.getLabel())
            .valueKey(request.getValueKey())
            .color(request.getColor())
            .icon(request.getIcon())
            .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
            .workflowSemanticKey(request.getWorkflowSemanticKey())
            .defaultOption(request.isDefaultOption())
            .active(true)
            .build();
        return toOptionDto(fieldOptionRepository.save(option));
    }

    @Transactional
    public FieldOptionDTO updateOption(Long fieldId, Long optionId, FieldOptionDTO request) {
        FieldOption option = fieldOptionRepository.findById(optionId)
            .orElseThrow(() -> new EntityNotFoundException("Field option not found: " + optionId));
        if (!option.getField().getId().equals(fieldId)) {
            throw new IllegalArgumentException("Option does not belong to field " + fieldId);
        }
        option.setLabel(request.getLabel());
        option.setColor(request.getColor());
        option.setIcon(request.getIcon());
        option.setSortOrder(request.getSortOrder());
        option.setWorkflowSemanticKey(request.getWorkflowSemanticKey());
        option.setDefaultOption(request.isDefaultOption());
        return toOptionDto(fieldOptionRepository.save(option));
    }

    @Transactional
    public void reorderOptions(Long fieldId, List<Long> orderedIds) {
        List<FieldOption> options = fieldOptionRepository.findByFieldIdOrderBySortOrderAsc(fieldId);
        for (int i = 0; i < orderedIds.size(); i++) {
            Long oid = orderedIds.get(i);
            int idx = i;
            options.stream()
                .filter(o -> o.getId().equals(oid))
                .findFirst()
                .ifPresent(o -> o.setSortOrder(idx));
        }
        fieldOptionRepository.saveAll(options);
    }

    // ── Mapper Helpers ───────────────────────────────────────────────────────

    public FieldDTO toDto(FieldDefinition field) {
        List<FieldOptionDTO> options = field.getOptions() == null ? List.of() :
            field.getOptions().stream().map(this::toOptionDto).toList();
        return FieldDTO.builder()
            .id(field.getId())
            .systemKey(field.getSystemKey())
            .name(field.getName())
            .description(field.getDescription())
            .fieldKind(field.getFieldKind())
            .fieldType(field.getFieldType())
            .system(field.isSystem())
            .locked(field.isLocked())
            .searchable(field.isSearchable())
            .active(field.isActive())
            .options(options)
            .build();
    }

    public FieldOptionDTO toOptionDto(FieldOption option) {
        return FieldOptionDTO.builder()
            .id(option.getId())
            .fieldId(option.getField() != null ? option.getField().getId() : null)
            .label(option.getLabel())
            .valueKey(option.getValueKey())
            .color(option.getColor())
            .icon(option.getIcon())
            .sortOrder(option.getSortOrder())
            .workflowSemanticKey(option.getWorkflowSemanticKey())
            .defaultOption(option.isDefaultOption())
            .active(option.isActive())
            .build();
    }

    public FieldDefinition getFieldById(Long id) {
        return fieldRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Field not found: " + id));
    }
}
