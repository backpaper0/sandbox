package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OAuth2Filter implements Filter {

    private String clientId;
    private String clientSecret;
    private String authorizationEndpoint;
    private String accessTokenEndpoint;
    private String userinfoEndpoint;
    private String redirectUri;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        clientId = filterConfig.getInitParameter("clientId");
        clientSecret = filterConfig.getInitParameter("clientSecret");
        authorizationEndpoint = filterConfig.getInitParameter("authorizationEndpoint");
        accessTokenEndpoint = filterConfig.getInitParameter("accessTokenEndpoint");
        userinfoEndpoint = filterConfig.getInitParameter("userinfoEndpoint");
        redirectUri = filterConfig.getInitParameter("redirectUri");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {

        if (Objects.equals(request.getRequestURI(), "/callback")) {

            final Map<String, String> queryParams = Arrays
                    .stream(request.getQueryString().split("&"))
                    .map(a -> a.split("="))
                    .collect(Collectors.toMap(a -> a[0],
                            a -> URLDecoder.decode(a[1], StandardCharsets.UTF_8)));

            final String code = queryParams.get("code");
            final String state = queryParams.get("state");

            final HttpSession session = request.getSession();

            final Object state0 = session.getAttribute("state");
            session.removeAttribute("state");
            if (Objects.equals(state, state0) == false) {
                response.sendError(400);
                return;
            }

            String accessToken;
            final HttpClient client = HttpClient.newHttpClient();
            try {

                final Map<String, String> requestParams = new HashMap<>();
                requestParams.put("grant_type", "authorization_code");
                requestParams.put("code", code);
                requestParams.put("redirect_uri", "http://localhost:8080/callback");
                requestParams.put("client_id", clientId);
                final String requestBody = requestParams.entrySet().stream()
                        .map(a -> a.getKey() + "="
                                + URLEncoder.encode(a.getValue(), StandardCharsets.UTF_8))
                        .collect(Collectors.joining("&"));

                final String authorization = "Basic " + Base64.getEncoder()
                        .encodeToString((clientId + ":" + clientSecret).getBytes());

                final HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(accessTokenEndpoint))
                        .method("POST", BodyPublishers.ofString(requestBody))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Authorization", authorization)
                        .header("Accept", "application/json").build();
                final HttpResponse<String> resp = client.send(req,
                        BodyHandlers.ofString(StandardCharsets.UTF_8));
                final Map<String, Object> json = (Map<String, Object>) JsonParser
                        .parse(resp.body());
                accessToken = (String) json.get("access_token");
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                final HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(userinfoEndpoint))
                        .header("Authorization", "Bearer " + accessToken)
                        .build();
                final HttpResponse<String> resp = client.send(req,
                        BodyHandlers.ofString(StandardCharsets.UTF_8));
                final Map<String, Object> json = (Map<String, Object>) JsonParser
                        .parse(resp.body());
                final String login = (String) json.get("login");
                session.setAttribute("userPrincipal", new PrincipalImpl(login));
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }

            final String location = (String) session.getAttribute("location");

            response.sendRedirect(location);

            return;
        }

        final HttpSession session = request.getSession();
        final Principal user = (Principal) session.getAttribute("userPrincipal");

        if (user == null) {

            final StringBuilder location = new StringBuilder();
            location.append(request.getRequestURI());
            if (request.getQueryString() != null) {
                location.append("?").append(request.getQueryString());
            }

            final String state = UUID.randomUUID().toString();
            session.setAttribute("state", state);
            session.setAttribute("location", location.toString());

            final Map<String, String> requestParams = new HashMap<>();
            requestParams.put("response_type", "code");
            requestParams.put("client_id", clientId);
            requestParams.put("redirect_uri", "http://localhost:8080/callback");
            requestParams.put("scope", "read:user");
            requestParams.put("state", state);
            final String requestBody = requestParams.entrySet().stream()
                    .map(a -> a.getKey() + "="
                            + URLEncoder.encode(a.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            response.sendRedirect(authorizationEndpoint + "?" + requestBody);
            return;
        }

        chain.doFilter(new AuthenticatedHttpServletRequestWrapper(request), response);
    }
}