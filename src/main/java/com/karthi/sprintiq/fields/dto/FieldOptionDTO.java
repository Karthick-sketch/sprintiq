package com.karthi.sprintiq.fields.dto;

import com.karthi.sprintiq.fields.enums.WorkflowSemanticKey;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldOptionDTO {

    private Long id;
    private Long fieldId;
    private String label;
    private String valueKey;
    private String color;
    private String icon;
    private Integer sortOrder;
    private WorkflowSemanticKey workflowSemanticKey;
    private boolean defaultOption;
    private boolean active;
}
