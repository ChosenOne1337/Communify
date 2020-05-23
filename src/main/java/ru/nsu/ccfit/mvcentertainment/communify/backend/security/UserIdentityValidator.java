package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.exceptions.AccessDeniedException;

@UtilityClass
public class UserIdentityValidator {

    public Long getActualUserId() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ((CustomUser) principal).getUserId();
    }

    public void validateUserId(Long userId) {
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
