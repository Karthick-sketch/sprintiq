package com.karthi.sprintiq.user.dto;

import com.karthi.sprintiq.user.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

  private Long id;
  private String name;
  private String email;
  private Role role;
  private Boolean active;
}
