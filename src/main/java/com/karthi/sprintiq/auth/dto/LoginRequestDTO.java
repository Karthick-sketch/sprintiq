package com.karthi.sprintiq.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

  private String email;
  private String password;
}
