package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/access_token")
public class AccessTokenEndpoint extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        String requestBody;
        try (ServletInputStream in = req.getInputStream()) {
            requestBody = new String(in.readAllBytes());
        }

        final Map<String, String> queryParams = Arrays
                .stream(requestBody.split("&"))
                .map(a -> a.split("="))
                .collect(Collectors.toMap(a -> a[0],
                        a -> URLDecoder.decode(a[1], StandardCharsets.UTF_8)));

        final String grantType = queryParams.get("grant_type");
        final String code = queryParams.get("code");
        final String redirectUri = queryParams.get("redirect_uri");
        final String clientId = queryParams.get("client_id");

        if (Objects.equals(grantType, "authorization_code") == false) {
            resp.sendError(400);
            return;
        }

        final Client client = Client.get(clientId);

        if (client.testRedirectUri(redirectUri) == false) {
            resp.sendError(400);
            return;
        }

        final String accessToken = AccessToken.getAccessToken(code);

        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.print("{\"access_token\":\"" + accessToken + "\"}");
        }
    }
}
