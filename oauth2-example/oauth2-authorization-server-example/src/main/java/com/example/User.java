package com.example;

import java.security.Principal;
import java.util.Objects;

public final class User implements Principal {

    private final String name;
    private final String password;

    public User(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean testPassword(final String arg) {
        return Objects.equals(arg, password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return name.equals(other.name);
    }
}
