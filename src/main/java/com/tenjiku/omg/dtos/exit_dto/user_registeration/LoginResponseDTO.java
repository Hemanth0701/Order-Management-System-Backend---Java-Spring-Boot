package com.tenjiku.omg.dtos.exit_dto.user_registeration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private UserDetailsResponseDTO userDetailsResponse ;
}
