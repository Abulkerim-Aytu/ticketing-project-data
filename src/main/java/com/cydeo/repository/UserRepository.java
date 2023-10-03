package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAllByIsDeletedOrderByFirstNameDesc(Boolean deleted);

    // get User based on username
    User findByUserNameAndIsDeleted(String username,Boolean deleted);

    // Delete user
    @Transactional
    void deleteByUserName(String username);

    // Get rolls
    List<User> findByRoleDescriptionIgnoreCaseAndIsDeleted(String description,Boolean deleted);

}
