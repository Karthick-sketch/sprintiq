package com.karthi.sprintiq.user;

import com.karthi.sprintiq.user.dto.UserRequestDTO;
import com.karthi.sprintiq.user.dto.UserResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(
    @RequestBody UserRequestDTO dto
  ) {
    UserResponseDTO created = userService.createUser(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(
    @PathVariable Long id,
    @RequestBody UserRequestDTO dto
  ) {
    return ResponseEntity.ok(userService.updateUser(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
