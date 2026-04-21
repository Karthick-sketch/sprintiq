package com.karthi.sprintiq.user;

import com.karthi.sprintiq.exception.UserNotFoundException;
import com.karthi.sprintiq.user.dto.UserDTO;
import com.karthi.sprintiq.user.dto.UserRequestDTO;
import com.karthi.sprintiq.user.entity.User;
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

  public User getUserById(Long id) {
    return userRepository
      .findById(id)
      .orElseThrow(() -> new UserNotFoundException(id));
  }

  public UserDTO getUserByIdToDTO(Long id) {
    User user = getUserById(id);
    return toDTO(user);
  }

  public UserDTO getUserByEmail(String email) {
    User user = userRepository
      .findByEmail(email)
      .orElseThrow(() ->
        new UserNotFoundException("User not found with email: " + email)
      );
    return toDTO(user);
  }

  public List<UserDTO> getAllUsers() {
    return userRepository.findAll().stream().map(this::toDTO).toList();
  }

  public UserDTO updateUser(Long id, UserRequestDTO dto) {
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
    return toDTO(updated);
  }

  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }
    userRepository.deleteById(id);
  }

  public UserDTO toDTO(User user) {
    return UserDTO.builder()
      .id(user.getId())
      .name(user.getName())
      .email(user.getEmail())
      .role(user.getRole())
      .build();
  }
}
