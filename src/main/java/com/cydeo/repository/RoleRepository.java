package com.cydeo.repository;

import com.cydeo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    // Give the role based on the description
    Role findByDescription(String description);
    // Optional<Role> indByDescription(String description); we can use this also
}
