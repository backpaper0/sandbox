package com.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DemoAuthorizationRepositoryImpl implements AuthorizationRepository {

    private final Map<String, Set<String>> value = Collections.synchronizedMap(new HashMap<>());

    @Override
    public boolean test(final User user, final Client client) {
        return value.getOrDefault(user.getName(), Collections.emptySet())
                .contains(client.getClientId());
    }

    @Override
    public void allow(final User user, final Client client) {
        value.computeIfAbsent(user.getName(),
                username -> Collections.synchronizedSet(new HashSet<>())).add(client.getClientId());
    }
}
