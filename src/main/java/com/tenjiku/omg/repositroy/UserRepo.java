package com.tenjiku.omg.repositroy;

import com.tenjiku.omg.entity.UserDetails;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserDetails,String> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(@NotBlank(message = "Phone number is required") @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number must be valid (10–15 digits, optional +)"
    ) String phoneNumber);
    @Query("SELECT u FROM UserDetails u WHERE (u.email = :input OR u.phoneNumber = :input) AND u.isDeleted = false")
    Optional<UserDetails> loginByPhoneNumberOrEmail(@Param("input") String phoneNumberOrEmail);

}
