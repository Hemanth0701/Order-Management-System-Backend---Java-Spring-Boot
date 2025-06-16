package com.tenjiku.omg.service.impl;

import com.tenjiku.omg.dtos.entry_dto.user_registeration.AdminDTO;
import com.tenjiku.omg.dtos.entry_dto.user_registeration.LoginRequestDTO;
import com.tenjiku.omg.dtos.entry_dto.user_registeration.UserDTO;
import com.tenjiku.omg.dtos.entry_dto.user_registeration.UserDetailsDTO;
import com.tenjiku.omg.dtos.exit_dto.user_registeration.LoginResponseDTO;
import com.tenjiku.omg.dtos.exit_dto.user_registeration.UserDetailsResponseDTO;
import com.tenjiku.omg.dtos.update_dto.user_registeration.AdminUpdateDTO;
import com.tenjiku.omg.dtos.update_dto.user_registeration.UserDetailsUpdateDTO;
import com.tenjiku.omg.dtos.update_dto.user_registeration.UserUpdateDTO;
import com.tenjiku.omg.entity.Admin;
import com.tenjiku.omg.entity.User;
import com.tenjiku.omg.entity.UserDetails;
import com.tenjiku.omg.entity.enums.Role;
import com.tenjiku.omg.exception.*;
import com.tenjiku.omg.mapper.UserDetailsEntryMapper;
import com.tenjiku.omg.mapper.UserDetailsExitMapper;
import com.tenjiku.omg.repositroy.UserRepo;
import com.tenjiku.omg.security.CustomUserDetails;
import com.tenjiku.omg.security.JwtUtil;
import com.tenjiku.omg.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
@AllArgsConstructor
@Transactional // applies to all methods
public class UserServiceImp implements UserService {

    private JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsEntryMapper userDetailsEntryMapper;
    private final UserDetailsExitMapper userDetailsExitMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDTO register(@NotNull UserDetailsDTO dto) {
        //  check if an email or PhoneNumber already exist
        if (userRepo.existsByPhoneNumber(dto.getPhoneNumber()) || userRepo.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("PhoneNumber or email already exists");
        }

        UserDetails savedUser;
        UserDetailsResponseDTO responseDTO;

        if (dto instanceof AdminDTO adminDTO) {
            Admin admin = userDetailsEntryMapper.toAdmin(adminDTO);
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            savedUser = userRepo.save(admin);

            responseDTO = userDetailsExitMapper.toAdminDTO(admin);
        } else if (dto instanceof UserDTO userDTO) {
            User user = userDetailsEntryMapper.toUser(userDTO);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            savedUser = userRepo.save(user);
            responseDTO = userDetailsExitMapper.toUserDTO(user);
        } else {
            throw new InternalServerErrorException("Unexpected user type: " + dto.getClass().getSimpleName());
        }

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String token = jwtUtil.generateToken(userDetails);
        return new LoginResponseDTO(token, responseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getPhoneNumberOrEmail(),
                        loginRequest.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        com.tenjiku.omg.entity.UserDetails appUser = userDetails.getUser();

        UserDetailsResponseDTO responseDTO = switch (appUser) {
            case User user -> userDetailsExitMapper.toUserDTO(user);
            case Admin admin -> userDetailsExitMapper.toAdminDTO(admin);
            default -> throw new IllegalStateException("Unexpected user type: " + appUser.getClass().getSimpleName());
        };

        String token = jwtUtil.generateToken(userDetails);

        // Return a combined DTO including token and user details
        return new LoginResponseDTO(token, responseDTO);
    }

    @Override
    public UserDetailsResponseDTO updateUser(String id,  UserDetailsUpdateDTO updatedUserDTO) {

        UserDetails existingUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (updatedUserDTO instanceof AdminUpdateDTO) {
            Admin admin = userDetailsEntryMapper.toAdmin((AdminUpdateDTO)  updatedUserDTO);
            return userDetailsExitMapper.toAdminDTO((Admin) userRepo.save(mergeUserDetails(existingUser,admin)));
        } else if (updatedUserDTO instanceof UserUpdateDTO) {
            User user = userDetailsEntryMapper.toUser((UserUpdateDTO)  updatedUserDTO);
            return userDetailsExitMapper.toUserDTO((User) userRepo.save(mergeUserDetails(existingUser,user)));
        } else
            throw new InternalServerErrorException(" Server Went Down ");
    }

    @Override
    public UserDetailsResponseDTO updatePassword(String userId, String password) {
        // Fetch user by ID
        UserDetails user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // set new password
        user.setPassword(password);

        // Save updated user & Convert to response DTO and Return
        return user.getRole() == Role.ADMIN ? userDetailsExitMapper.toUserDetailsDTO((Admin) userRepo.save(user)) : userDetailsExitMapper.toUserDetailsDTO((User) userRepo.save(user));

    }

    public UserDetails mergeUserDetails(UserDetails existingUser, @NotNull UserDetails updatedUser){
        // Update fields — only if they're provided (null-safe)
        if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPhoneNumber() != null) existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        if (updatedUser.getDob() != null) existingUser.setDob(updatedUser.getDob());
        return existingUser;
    }

    @Override
    public String deleteUser(String id) {
        UserDetails user = userRepo.findById(id)
                .orElseThrow(() ->  new UserNotFoundException("User not found with ID: " + id));
        if (user.isDeleted()) {
            throw new UserAlreadyDeletedException("User is already deleted.");

        }
        user.setDeleted(true);
        user.setDeletedAt(Instant.now());
        userRepo.save(user);

        return "Deleted successfully";
    }

}
