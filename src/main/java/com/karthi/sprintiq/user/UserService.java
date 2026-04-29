package com.karthi.sprintiq.user;

import com.karthi.sprintiq.exception.UserNotFoundException;
import com.karthi.sprintiq.user.dto.UserDTO;
import com.karthi.sprintiq.user.entity.User;
import com.karthi.sprintiq.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

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

  public UserDTO updateUser(Long id, UserDTO dto) {
    User user = getUserById(id);
    if (dto.getName() != null && !dto.getName().isBlank()) {
      user.setName(dto.getName());
    }
    if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
      user.setEmail(dto.getEmail());
    }
    if (dto.getRole() != null) {
      user.setRole(dto.getRole());
    }
    if (dto.getActive() != null) {
      user.setActive(dto.getActive());
    }
    return toDTO(userRepository.save(user));
  }

  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }
    userRepository.deleteById(id);
  }

  public UserDTO toDTO(User user) {
    if (user == null) {
      return null;
    }
    return UserDTO.builder()
      .id(user.getId())
      .name(user.getName())
      .email(user.getEmail())
      .role(user.getRole())
      .active(user.getActive())
      .build();
  }
}
