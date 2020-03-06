package com.example;

import java.util.Objects;

public final class Client {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public Client(final String clientId, final String clientSecret, final String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public boolean testRedirectUri(final String arg) {
        return arg != null && arg.startsWith(redirectUri);
    }

    public boolean testClientSecret(final String arg) {
        return Objects.equals(arg, clientSecret);
    }
}
