package com.flex.task_tracker.app.repositories.user;

import com.flex.task_tracker.app.entities.user.ExpiredToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpiredTokenRepository extends JpaRepository<ExpiredToken, String> {

    boolean existsById(String token);
}
