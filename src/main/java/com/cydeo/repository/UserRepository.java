package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // get User based on username
    User findByUserName(String username);

    // Delete user
    @Transactional
    void deleteByUserName(String username);

    // Get rolls
    List<User> findByRoleDescriptionIgnoreCase(String description);

}
