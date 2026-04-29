package com.karthi.sprintiq.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequestDTO {

  private String refreshToken;
}
