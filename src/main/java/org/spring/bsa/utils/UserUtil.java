package org.spring.bsa.utils;

import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    public boolean checkUserID(String userId) {
        return userId.isBlank() || userId.isEmpty();
    }

}
