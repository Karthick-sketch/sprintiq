package com.karthi.sprintiq.user.dto;

import com.karthi.sprintiq.user.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {

  private String name;
  private String email;
  private String password;
  private Role role;
}
