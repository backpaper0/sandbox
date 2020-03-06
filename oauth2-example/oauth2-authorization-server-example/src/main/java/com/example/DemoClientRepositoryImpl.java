package com.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DemoClientRepositoryImpl implements ClientRepository {

    private final Map<String, Client> clients = Collections.synchronizedMap(new HashMap<>());

    public void add(final Client client) {
        clients.put(client.getClientId(), client);
    }

    @Override
    public Client find(final String clientId) {
        return clients.get(clientId);
    }
}
