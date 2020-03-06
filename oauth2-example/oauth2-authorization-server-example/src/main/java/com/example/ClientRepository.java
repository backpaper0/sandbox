package com.example;

import javax.servlet.ServletContext;

public interface ClientRepository {

    static ClientRepository get(final ServletContext sc) {
        return (ClientRepository) sc.getAttribute(ClientRepository.class.getName());
    }

    static void set(final ServletContext sc, final ClientRepository clientRepository) {
        sc.setAttribute(ClientRepository.class.getName(), clientRepository);
    }

    Client find(String clientId);
}
