package com.karthi.sprintiq.user.dto;

import com.karthi.sprintiq.user.enums.Role;
import com.karthi.sprintiq.user.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

  private Long id;
  private String name;
  private String email;
  private Role role;
  private UserStatus status;
}
