package com.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public static synchronized String getAndRemoveAccessToken(final String code) {
        final String accessToken = codeAndAccessTokens.get(code);
        codeAndAccessTokens.remove(code);
        return accessToken;
    }

    public static String getUser(final String accessToken) {
        return accessTokenAndUsernames.get(accessToken);
    }
}
