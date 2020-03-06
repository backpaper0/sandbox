package com.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class AccessToken {

    private static final Map<String, String> codeAndAccessTokens = Collections
            .synchronizedMap(new HashMap<>());

    private static final Map<String, String> accessTokenAndUsernames = Collections
            .synchronizedMap(new HashMap<>());

    public static void associateCodeAndAccessToken(final String code, final String accessToken) {
        codeAndAccessTokens.put(code, accessToken);
    }

    public static void associateAccessTokenAndUsername(final String accessToken,
            final String username) {
        accessTokenAndUsernames.put(accessToken, username);
    }

    public static String getAccessToken(final String code) {
        return codeAndAccessTokens.get(code);
    }

    public static User getUser(final String accessToken) {
        return Optional.ofNullable(accessTokenAndUsernames.get(accessToken)).map(User::get)
                .orElse(null);
    }
}
