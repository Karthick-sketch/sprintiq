package com.karthi.sprintiq.fields.dto;

import com.karthi.sprintiq.fields.enums.WorkflowSemanticKey;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

/** Dynamic ticket search request supporting workflow semantic and typed field filters. */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketSearchRequest {

    private List<Long> projectIds;

    /** Cross-project status filter using semantic keys — drives burndown and board filtering. */
    private List<WorkflowSemanticKey> workflowSemanticKeys;

    private List<FieldFilter> fieldFilters;

    private Integer page;
    private Integer size;
    private List<SortSpec> sort;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldFilter {
        /** Use systemKey for standard fields (e.g. "priority") or fieldId for custom fields. */
        private String systemKey;
        private Long fieldId;
        private FilterOperator operator;

        // Value slots — only populate the one matching the operator type
        private List<String> optionValueKeys;
        private List<Long> userIds;
        private BigDecimal numberValue;
        private String textValue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortSpec {
        private String field;
        private String direction; // ASC | DESC
    }

    public enum FilterOperator {
        IN_OPTIONS,
        NOT_IN_OPTIONS,
        IN_USERS,
        TEXT_CONTAINS,
        NUMBER_EQ,
        NUMBER_GTE,
        NUMBER_LTE,
        DATE_ON,
        DATE_AFTER,
        DATE_BEFORE,
        IS_EMPTY,
        IS_NOT_EMPTY
    }
}
