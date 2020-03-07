package com.example;

import javax.servlet.ServletContext;

public interface AuthorizationRepository {

    static AuthorizationRepository get(final ServletContext sc) {
        return (AuthorizationRepository) sc.getAttribute(AuthorizationRepository.class.getName());
    }

    static void set(final ServletContext sc,
            final AuthorizationRepository authorizationRepository) {
        sc.setAttribute(AuthorizationRepository.class.getName(), authorizationRepository);
    }

    boolean test(User user, Client client);

    void allow(User user, Client client);
}
