package com.flex.task_tracker.app.repositories.user;

import com.flex.task_tracker.app.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmailAndDeletedIsFalse(String email);

    User findByIdAndDeletedIsFalse(Integer id);

    User findByEmailAndDeletedIsFalse(String email);

    @Query("SELECT u FROM User u " +
            "WHERE u.role = 'SUPER_ADMIN' AND u.deleted = false")
    User onlyOneAdmin();
}
