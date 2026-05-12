package com.karthi.sprintiq.fields.dto;

import com.karthi.sprintiq.fields.enums.FieldType;
import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectFieldResponse {

    private Long projectFieldId;
    private Long fieldId;
    private String systemKey;
    private String name;
    private String displayName;
    private String helpText;
    private FieldType fieldType;
    private Integer sortOrder;
    private boolean enabled;
    private boolean required;
    private boolean useGlobalOptions;
    private List<FieldOptionDTO> options;
}
