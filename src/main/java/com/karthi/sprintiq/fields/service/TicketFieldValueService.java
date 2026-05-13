package com.karthi.sprintiq.fields.service;

import com.karthi.sprintiq.exception.EntityNotFoundException;
import com.karthi.sprintiq.fields.dto.TicketFieldValueRequest;
import com.karthi.sprintiq.fields.dto.TicketFieldValueRequest.FieldValueItemRequest;
import com.karthi.sprintiq.fields.dto.TicketFieldValueResponse;
import com.karthi.sprintiq.fields.dto.TicketFieldValueResponse.FieldValueItemResponse;
import com.karthi.sprintiq.fields.entity.FieldOption;
import com.karthi.sprintiq.fields.entity.ProjectField;
import com.karthi.sprintiq.fields.entity.ProjectFieldOption;
import com.karthi.sprintiq.fields.entity.TicketFieldValue;
import com.karthi.sprintiq.fields.repository.FieldOptionRepository;
import com.karthi.sprintiq.fields.repository.ProjectFieldOptionRepository;
import com.karthi.sprintiq.fields.repository.ProjectFieldRepository;
import com.karthi.sprintiq.fields.repository.TicketFieldValueRepository;
import com.karthi.sprintiq.tickets.repository.TicketsRepository;
import com.karthi.sprintiq.user.entity.User;
import com.karthi.sprintiq.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketFieldValueService {

    private final TicketFieldValueRepository ticketFieldValueRepository;
    private final ProjectFieldRepository projectFieldRepository;
    private final FieldOptionRepository fieldOptionRepository;
    private final ProjectFieldOptionRepository projectFieldOptionRepository;
    private final TicketsRepository ticketsRepository;
    private final UserRepository userRepository;

    // ── Read ─────────────────────────────────────────────────────────────────

    public List<TicketFieldValueResponse> getFieldValues(Long ticketId) {
        List<TicketFieldValue> rows =
            ticketFieldValueRepository.findByTicketIdOrderByProjectFieldIdAscValueIndexAsc(ticketId);

        // Group by project field
        Map<Long, List<TicketFieldValue>> grouped = rows.stream()
            .collect(Collectors.groupingBy(r -> r.getProjectField().getId()));

        return grouped.entrySet().stream().map(entry -> {
            ProjectField pf = entry.getValue().get(0).getProjectField();
            List<FieldValueItemResponse> items = entry.getValue().stream()
                .map(this::toItemResponse).toList();
            return TicketFieldValueResponse.builder()
                .projectFieldId(pf.getId())
                .systemKey(pf.getField().getSystemKey())
                .displayName(pf.getDisplayName() != null ? pf.getDisplayName() : pf.getField().getName())
                .fieldType(pf.getField().getFieldType())
                .values(items)
                .build();
        }).toList();
    }

    // ── Write ─────────────────────────────────────────────────────────────────

    /**
     * Full replacement: replaces ALL field values for the listed fields in one transaction.
     */
    @Transactional
    public List<TicketFieldValueResponse> saveFieldValues(Long ticketId, List<TicketFieldValueRequest> requests) {
        validateTicketExists(ticketId);
        for (TicketFieldValueRequest req : requests) {
            replaceFieldValues(ticketId, req);
        }
        return getFieldValues(ticketId);
    }

    /**
     * Single-field patch: replaces values for one projectField only.
     */
    @Transactional
    public TicketFieldValueResponse patchFieldValue(Long ticketId, Long projectFieldId,
                                                     TicketFieldValueRequest request) {
        validateTicketExists(ticketId);
        replaceFieldValues(ticketId, request);
        return getFieldValues(ticketId).stream()
            .filter(r -> r.getProjectFieldId().equals(projectFieldId))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("No value found for projectFieldId " + projectFieldId));
    }

    // ── Internals ─────────────────────────────────────────────────────────────

    private void replaceFieldValues(Long ticketId, TicketFieldValueRequest req) {
        ProjectField pf = projectFieldRepository.findById(req.getProjectFieldId())
            .orElseThrow(() -> new EntityNotFoundException("ProjectField not found: " + req.getProjectFieldId()));

        // Validate required
        if (pf.isRequired() && (req.getValues() == null || req.getValues().isEmpty())) {
            throw new IllegalArgumentException("Field '" + pf.getField().getName() + "' is required.");
        }

        // Delete existing values for this field
        ticketFieldValueRepository.deleteByTicketIdAndProjectFieldId(ticketId, pf.getId());

        if (req.getValues() == null || req.getValues().isEmpty()) return;

        // Insert typed rows
        List<TicketFieldValue> rows = new ArrayList<>();
        List<FieldValueItemRequest> items = req.getValues();
        for (int i = 0; i < items.size(); i++) {
            FieldValueItemRequest item = items.get(i);
            TicketFieldValue row = buildRow(ticketId, pf, item, i);
            rows.add(row);
        }
        ticketFieldValueRepository.saveAll(rows);
    }

    private TicketFieldValue buildRow(Long ticketId, ProjectField pf,
                                      FieldValueItemRequest item, int index) {
        TicketFieldValue.TicketFieldValueBuilder b = TicketFieldValue.builder()
            .ticketId(ticketId)
            .projectField(pf)
            .valueIndex(index);

        if (item.getTextValue() != null)     { b.textValue(item.getTextValue()); return b.build(); }
        if (item.getNumberValue() != null)   { b.numberValue(item.getNumberValue()); return b.build(); }
        if (item.getDateValue() != null)     { b.dateValue(item.getDateValue()); return b.build(); }
        if (item.getDatetimeValue() != null) { b.datetimeValue(item.getDatetimeValue()); return b.build(); }
        if (item.getBooleanValue() != null)  { b.booleanValue(item.getBooleanValue()); return b.build(); }
        if (item.getFieldOptionId() != null) {
            FieldOption fo = fieldOptionRepository.findById(item.getFieldOptionId())
                .orElseThrow(() -> new EntityNotFoundException("FieldOption not found: " + item.getFieldOptionId()));
            b.fieldOption(fo);
            return b.build();
        }
        if (item.getProjectFieldOptionId() != null) {
            ProjectFieldOption pfo = projectFieldOptionRepository.findById(item.getProjectFieldOptionId())
                .orElseThrow(() -> new EntityNotFoundException("ProjectFieldOption not found: " + item.getProjectFieldOptionId()));
            b.projectFieldOption(pfo);
            return b.build();
        }
        if (item.getUserId() != null) {
            User user = userRepository.findById(item.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + item.getUserId()));
            b.user(user);
            return b.build();
        }
        throw new IllegalArgumentException("FieldValueItemRequest must have exactly one non-null value.");
    }

    private FieldValueItemResponse toItemResponse(TicketFieldValue row) {
        FieldValueItemResponse.FieldValueItemResponseBuilder b = FieldValueItemResponse.builder()
            .textValue(row.getTextValue())
            .numberValue(row.getNumberValue())
            .dateValue(row.getDateValue())
            .datetimeValue(row.getDatetimeValue())
            .booleanValue(row.getBooleanValue());

        if (row.getFieldOption() != null) {
            FieldOption fo = row.getFieldOption();
            b.fieldOptionId(fo.getId())
             .optionLabel(fo.getLabel())
             .optionValueKey(fo.getValueKey())
             .optionColor(fo.getColor())
             .workflowSemanticKey(fo.getWorkflowSemanticKey());
        }
        if (row.getProjectFieldOption() != null) {
            ProjectFieldOption pfo = row.getProjectFieldOption();
            b.fieldOptionId(pfo.getId())
             .optionLabel(pfo.getLabel())
             .optionValueKey(pfo.getValueKey())
             .optionColor(pfo.getColor())
             .workflowSemanticKey(pfo.getWorkflowSemanticKey());
        }
        if (row.getUser() != null) {
            b.userId(row.getUser().getId()).userName(row.getUser().getName());
        }
        return b.build();
    }

    private void validateTicketExists(Long ticketId) {
        if (!ticketsRepository.existsById(ticketId)) {
            throw new EntityNotFoundException("Ticket not found: " + ticketId);
        }
    }
}
