package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.exceptions.AccessDeniedException;

public class UserIdentityValidator {

    public static Long getActualUserId() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ((CustomUserDetails) principal).getUserId();
    }

    public static void validateUserId(Long userId) throws AccessDeniedException {
        Long actualUserId = getActualUserId();
        if (!actualUserId.equals(userId)) {
            throw new AccessDeniedException(
                String.format(
                    "Actual user id (%s) does not correspond to the specified one (%s)",
                    getActualUserId(), userId
                )
            );
        }
    }

}
