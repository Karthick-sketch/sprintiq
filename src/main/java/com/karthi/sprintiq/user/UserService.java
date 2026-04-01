package com.karthi.sprintiq.user;

import com.karthi.sprintiq.user.dto.UserRequestDTO;
import com.karthi.sprintiq.user.dto.UserResponseDTO;
import com.karthi.sprintiq.user.entity.User;
import com.karthi.sprintiq.user.enums.Role;
import com.karthi.sprintiq.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserResponseDTO createUser(UserRequestDTO dto) {
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new RuntimeException("Email already exists: " + dto.getEmail());
    }

    User user = User.builder()
      .name(dto.getName())
      .email(dto.getEmail())
      .password(passwordEncoder.encode(dto.getPassword()))
      .role(dto.getRole() != null ? dto.getRole() : Role.USER)
      .build();

    User saved = userRepository.save(user);
    return toResponseDto(saved);
  }

  public UserResponseDTO getUserById(Long id) {
    User user = userRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    return toResponseDto(user);
  }

  public UserResponseDTO getUserByEmail(String email) {
    User user = userRepository
      .findByEmail(email)
      .orElseThrow(() ->
        new RuntimeException("User not found with email: " + email)
      );
    return toResponseDto(user);
  }

  public List<UserResponseDTO> getAllUsers() {
    return userRepository.findAll().stream().map(this::toResponseDto).toList();
  }

  public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
    User user = userRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }
    if (dto.getRole() != null) {
      user.setRole(dto.getRole());
    }

    User updated = userRepository.save(user);
    return toResponseDto(updated);
  }

  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }

  private UserResponseDTO toResponseDto(User user) {
    return UserResponseDTO.builder()
      .id(user.getId())
      .name(user.getName())
      .email(user.getEmail())
      .role(user.getRole())
      .createdAt(user.getCreatedAt())
      .build();
  }
}
