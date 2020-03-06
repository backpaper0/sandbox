package com.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DemoUserRepositoryImpl implements UserRepository {

    private final Map<String, User> users = Collections.synchronizedMap(new HashMap<>());

    public void add(final User user) {
        users.put(user.getName(), user);
    }

    @Override
    public User find(final String username) {
        return users.get(username);
    }
}
