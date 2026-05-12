package com.karthi.sprintiq.fields.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import lombok.*;

/** Groups submitted values for a single project field. */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketFieldValueRequest {

    /** Must reference a project_fields row that belongs to this ticket's project. */
    private Long projectFieldId;

    /** One item for single-value fields; multiple items for MULTI_DROPDOWN / MULTI_USER. */
    private List<FieldValueItemRequest> values;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldValueItemRequest {
        private String textValue;
        private BigDecimal numberValue;
        private LocalDate dateValue;
        private Instant datetimeValue;
        private Boolean booleanValue;
        private Long fieldOptionId;
        private Long projectFieldOptionId;
        private Long userId;
    }
}
