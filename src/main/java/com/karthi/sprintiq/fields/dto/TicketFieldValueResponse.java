package com.karthi.sprintiq.fields.dto;

import com.karthi.sprintiq.fields.enums.FieldType;
import com.karthi.sprintiq.fields.enums.WorkflowSemanticKey;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketFieldValueResponse {

    private Long projectFieldId;
    private String systemKey;
    private String displayName;
    private FieldType fieldType;
    private List<FieldValueItemResponse> values;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldValueItemResponse {
        private String textValue;
        private BigDecimal numberValue;
        private LocalDate dateValue;
        private Instant datetimeValue;
        private Boolean booleanValue;
        private Long fieldOptionId;
        private String optionLabel;
        private String optionValueKey;
        private String optionColor;
        private WorkflowSemanticKey workflowSemanticKey;
        private Long userId;
        private String userName;
    }
}
