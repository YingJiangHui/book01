package org.ying.book.Context;


import org.ying.book.dto.user.UserJwtDto;

public class UserContext {
    private static final ThreadLocal<UserJwtDto> currentUser = new ThreadLocal<>();

    public static UserJwtDto getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(UserJwtDto user) {
        currentUser.set(user);
    }

    public static void clear() {
        currentUser.remove();
    }
}