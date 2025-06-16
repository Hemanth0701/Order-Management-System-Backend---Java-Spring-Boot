package com.tenjiku.omg.security;

import com.tenjiku.omg.repositroy.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return userRepo
                .loginByPhoneNumberOrEmail(identifier)
                .map(CustomUserDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email or phone: " + identifier));
    }
}







