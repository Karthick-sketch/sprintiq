package com.karthi.sprintiq.user;

import com.karthi.sprintiq.user.dto.UserDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserByIdToDTO(id));
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PostMapping
  public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
    return ResponseEntity.ok(userService.createUser(dto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> updateUser(
    @PathVariable Long id,
    @RequestBody UserDTO dto
  ) {
    return ResponseEntity.ok(userService.updateUser(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
