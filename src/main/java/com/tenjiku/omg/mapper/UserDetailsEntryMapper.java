package com.tenjiku.omg.mapper;

import com.tenjiku.omg.dtos.entry_dto.user_registeration.AdminDTO;
import com.tenjiku.omg.dtos.entry_dto.user_registeration.UserDTO;
import com.tenjiku.omg.dtos.update_dto.user_registeration.AdminUpdateDTO;
import com.tenjiku.omg.dtos.update_dto.user_registeration.UserUpdateDTO;
import com.tenjiku.omg.entity.Admin;
import com.tenjiku.omg.entity.User;
import com.tenjiku.omg.entity.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsEntryMapper {

    public User toUser(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDob(dto.getDob());
        user.setRole(Role.USER);
        return user;
    }

    public Admin toAdmin(AdminDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Admin admin =new Admin();
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        admin.setPhoneNumber(dto.getPhoneNumber());
        admin.setDob(dto.getDob());
        admin.setRole(Role.ADMIN);

        return admin;
    }
    public User toUser(UserUpdateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDob(dto.getDob());
        user.setRole(Role.USER);

        return user;
    }

    public Admin toAdmin(AdminUpdateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Admin admin =new Admin();

        admin.setEmail(dto.getEmail());
        admin.setPhoneNumber(dto.getPhoneNumber());
        admin.setDob(dto.getDob());
        admin.setRole(Role.ADMIN);

        return admin;
    }
}
