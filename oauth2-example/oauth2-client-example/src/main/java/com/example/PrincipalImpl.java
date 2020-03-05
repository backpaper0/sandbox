package com.example;

import java.io.Serializable;
import java.security.Principal;
import java.util.Objects;

public final class PrincipalImpl implements Principal, Serializable {

    private final String name;

    public PrincipalImpl(final String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName() {
        return name;
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
        final PrincipalImpl other = (PrincipalImpl) obj;
        return name.equals(other.name);
    }
}
