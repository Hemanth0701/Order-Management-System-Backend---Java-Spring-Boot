package com.tenjiku.omg.repositroy;

import com.tenjiku.omg.entity.Cart;
import com.tenjiku.omg.entity.User;
import com.tenjiku.omg.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,String> {

    Optional<Cart> findByUserAndCurrentstatus(User user, Status status);
}
