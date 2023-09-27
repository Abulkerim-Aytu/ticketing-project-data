package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // get User based on username
    User findByUserName(String username);

    // Delete user
    void deleteByUserName(String username);
}
