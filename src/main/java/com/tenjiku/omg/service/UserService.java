package com.tenjiku.omg.service;

import com.tenjiku.omg.dtos.entry_dto.user_registeration.LoginRequestDTO;
import com.tenjiku.omg.dtos.entry_dto.user_registeration.UserDetailsDTO;
import com.tenjiku.omg.dtos.exit_dto.user_registeration.LoginResponseDTO;
import com.tenjiku.omg.dtos.exit_dto.user_registeration.UserDetailsResponseDTO;
import com.tenjiku.omg.dtos.update_dto.user_registeration.UserDetailsUpdateDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    LoginResponseDTO register(@Valid UserDetailsDTO userDetailsDTO);

    Object deleteUser(String id);

    LoginResponseDTO login(LoginRequestDTO loginRequest);

    UserDetailsResponseDTO updateUser(String id, @Valid UserDetailsUpdateDTO userDetailsUpdateDTO);

    UserDetailsResponseDTO updatePassword(@NotNull String userId, @NotBlank(message = "Password is required") @Pattern(
                                                        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
                                                        message = "Password must be at least 8 characters, include one uppercase letter, one digit, and one special character") String password);
}
