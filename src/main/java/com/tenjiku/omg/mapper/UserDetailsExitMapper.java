package com.tenjiku.omg.mapper;

import com.tenjiku.omg.dtos.exit_dto.user_registeration.AdminResponseDTO;
import com.tenjiku.omg.dtos.exit_dto.user_registeration.UserDetailsResponseDTO;
import com.tenjiku.omg.dtos.exit_dto.user_registeration.UserResponseDTO;
import com.tenjiku.omg.entity.Admin;
import com.tenjiku.omg.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsExitMapper {

    public UserResponseDTO toUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userResponseDTO.setDob(user.getDob());
        userResponseDTO.setRole(String.valueOf(user.getRole()));
        userResponseDTO.setCreatedAt(user.getCreatedAt());
        return userResponseDTO;

    }

    public AdminResponseDTO toAdminDTO(Admin admin) {
        if ( admin == null ) {
            return null;
        }
        AdminResponseDTO adminResponseDTO = new AdminResponseDTO();
        adminResponseDTO.setId(admin.getId());
        adminResponseDTO.setUsername(admin.getUsername());
        adminResponseDTO.setEmail(admin.getEmail());
        adminResponseDTO.setPhoneNumber(admin.getPhoneNumber());
        adminResponseDTO.setDob(admin.getDob());
        adminResponseDTO.setRole(String.valueOf(admin.getRole()));
        adminResponseDTO.setCreatedAt(admin.getCreatedAt());

        return adminResponseDTO;
    }

    public UserDetailsResponseDTO toUserDetailsDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDetailsResponseDTO.UserDetailsResponseDTOBuilder userDTO = UserDetailsResponseDTO.builder();
        userDTO.id(user.getId());
        userDTO.username(user.getUsername() );
        userDTO.email( user.getEmail()  );
        userDTO.phoneNumber( user.getPhoneNumber() );
        userDTO.Dob(user.getDob());
        userDTO.role( user.getRole().name() );
        userDTO.createdAt(user.getCreatedAt());

        return userDTO.build();
    }

    public UserDetailsResponseDTO toUserDetailsDTO(Admin admin) {
        if ( admin == null ) {
            return null;
        }

        UserDetailsResponseDTO.UserDetailsResponseDTOBuilder adminDTO = UserDetailsResponseDTO.builder();
        adminDTO.id( admin.getId() );
        adminDTO.username( admin.getUsername() );
        adminDTO.email( admin.getEmail() );
        adminDTO.phoneNumber(admin.getPhoneNumber() );
        adminDTO.role( admin.getRole().name() );
        adminDTO.createdAt( admin.getCreatedAt() );

        return adminDTO.build() ;
    }
}
