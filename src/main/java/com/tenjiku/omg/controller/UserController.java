package com.tenjiku.omg.controller;

import com.tenjiku.omg.dtos.exit_dto.user_registeration.UserDetailsResponseDTO;
import com.tenjiku.omg.dtos.update_dto.user_registeration.UserDetailsUpdateDTO;
import com.tenjiku.omg.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable @NotNull String id,
                                        @Valid @RequestBody UserDetailsUpdateDTO userDetailsUpdateDTO){
        UserDetailsResponseDTO updatedUser= userService.updateUser(id,userDetailsUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(value = "/forgotPassword/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> updateUserPassword(@PathVariable @NotNull String userId,
                                                @RequestParam  @NotBlank(message = "Password is required")
                                                @Pattern(
                                                        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                                                        message = "Password must be at least 8 characters, include one uppercase letter, one digit, and one special character")
                                                String password){
        return ResponseEntity.ok(userService.updatePassword(userId,password));
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
