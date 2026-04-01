package com.karthi.sprintiq.user.dto;

import com.karthi.sprintiq.user.enums.Role;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

  private Long id;
  private String name;
  private String email;
  private Role role;
  private LocalDateTime createdAt;
}
