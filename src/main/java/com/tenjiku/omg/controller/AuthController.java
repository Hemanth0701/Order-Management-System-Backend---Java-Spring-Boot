package com.tenjiku.omg.controller;

import com.tenjiku.omg.dtos.entry_dto.user_registeration.LoginRequestDTO;
import com.tenjiku.omg.dtos.entry_dto.user_registeration.UserDetailsDTO;
import com.tenjiku.omg.dtos.exit_dto.user_registeration.LoginResponseDTO;
import com.tenjiku.omg.dtos.exit_dto.user_registeration.UserDetailsResponseDTO;
import com.tenjiku.omg.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {

        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> userRegister(@Valid @RequestBody UserDetailsDTO userDetailsDTO){

        LoginResponseDTO response = userService.register(userDetailsDTO); // return both token + user

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(response.getUserDetailsResponse().getUsername())
                .toUri();

        return ResponseEntity.created(location).body(response);// if there is no any other instance
    }
}
