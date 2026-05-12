package com.karthi.sprintiq.fields.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectFieldRequest {

    private Long fieldId;
    private String displayName;
    private String helpText;
    private Integer sortOrder;
    private boolean enabled;
    private boolean required;
    private boolean useGlobalOptions;
}
