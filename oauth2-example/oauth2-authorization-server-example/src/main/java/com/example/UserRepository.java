package com.example;

import javax.servlet.ServletContext;

public interface UserRepository {

    static UserRepository get(final ServletContext sc) {
        return (UserRepository) sc.getAttribute(UserRepository.class.getName());
    }

    static void set(final ServletContext sc, final UserRepository userRepository) {
        sc.setAttribute(UserRepository.class.getName(), userRepository);
    }

    User find(String username);
}
