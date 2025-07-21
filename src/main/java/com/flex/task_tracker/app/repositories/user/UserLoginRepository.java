package com.flex.task_tracker.app.repositories.user;

import com.flex.task_tracker.app.entities.user.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserLoginRepository extends JpaRepository<UserLogin, Integer> {

    @Query("select u from UserLogin u where u.user.id=:userId and u.logoutTime is null")
    List<UserLogin> getUserLogins(@Param("userId") Integer userId);
}
