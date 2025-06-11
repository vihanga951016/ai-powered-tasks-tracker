package com.flex.task_tracker.app.services.users.impls;

import com.flex.task_tracker.app.entities.ExpiredToken;
import com.flex.task_tracker.app.entities.User;
import com.flex.task_tracker.app.entities.UserLogin;
import com.flex.task_tracker.app.repositories.ExpiredTokenRepository;
import com.flex.task_tracker.app.repositories.UserLoginRepository;
import com.flex.task_tracker.app.repositories.UserRepository;
import com.flex.task_tracker.security.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
@SuppressWarnings("Duplicates")
public class UserHelper {

    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;
    private final ExpiredTokenRepository expiredTokenRepository;

    public void logoutPreviousLogins(Integer userId) {
        log.info("userId " + userId);

        List<UserLogin> userLogins = userLoginRepository.getUserLogins(userId);
        log.info("logins: " + userLogins.size());

        if (userLogins.size() > 0) {
            for (UserLogin userLogin: userLogins) {
                log.info("user-login-id " + userLogin.getId());
                userLogin.setLogoutTime(new Date());
                userLoginRepository.save(userLogin);

                ExpiredToken expiredToken = ExpiredToken.builder()
                        .id(userLogin.getToken())
                        .userId(userId)
                        .build();

                expiredTokenRepository.save(expiredToken);
            }
        }
    }

    public User updatingUserData(User existingData, User requestingData){
        if (requestingData.getFirstName() != null
                && !requestingData.getFirstName().equals(existingData.getFirstName())) {
            existingData.setFirstName(requestingData.getFirstName());
        }

        if (requestingData.getLastName() != null
                && !requestingData.getLastName().equals(existingData.getLastName())) {
            existingData.setLastName(requestingData.getLastName());
        }

        if (requestingData.getType() != null
                && !requestingData.getType().equals(existingData.getType())) {
            existingData.setFirstName(requestingData.getType());
        }

        return existingData;
    }

    public boolean authorizingUser(User user, HttpServletRequest request) {
        String email = JwtUtil.extractUsername(request);

        if (email == null) {
            log.error("user authorizing - email is null");
            return false;
        }

        User claimedUser = userRepository.findByEmailAndDeletedIsFalse(email);

        if (claimedUser == null) {
            log.error("user authorizing - claimed user is null => " + email);
            return false;
        }

        if (!claimedUser.getId().equals(user.getId())) {
            log.error("user authorizing - unauthorized => claimed user | "
                    + claimedUser.getId() + " | user data | " + user.getId());
            return false;
        }

        return true;
    }
}
