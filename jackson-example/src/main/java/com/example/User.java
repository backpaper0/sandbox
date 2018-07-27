package com.example;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "username", "roles" })
public class User {

    private final String username;
    private final Set<String> roles;

    @JsonCreator
    public User(@JsonProperty("username") final String username,
            @JsonProperty("roles") final Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public User(final String username, final String... roles) {
        this(username, Stream.of(roles).collect(Collectors.toSet()));
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, roles);
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
        return Objects.equals(username, other.username)
                && Objects.equals(roles, other.roles);
    }

    @Override
    public String toString() {
        return String.format("User(%s, %s)", username, roles);
    }
}
