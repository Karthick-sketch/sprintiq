package com.karthi.sprintiq.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

  private String name;
  private String email;
  private String password;
}
