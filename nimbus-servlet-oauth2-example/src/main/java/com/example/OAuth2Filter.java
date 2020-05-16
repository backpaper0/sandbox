package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import com.nimbusds.oauth2.sdk.AuthorizationResponse;
import com.nimbusds.oauth2.sdk.AuthorizationSuccessResponse;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.Tokens;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

public class OAuth2Filter implements Filter {

	private URI authzURI;
	private ClientID clientID;
	private String scope;
	private URI tokenURI;
	private URI usernameURI;
	private String usernameJSONKey;
	private Secret clientSecret;
	private final URI callbackURI = URI.create("http://localhost:8080/oauth2/callback");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.authzURI = URI.create(filterConfig.getInitParameter("authzURI"));
		this.clientID = new ClientID(filterConfig.getInitParameter("clientID"));
		this.scope = filterConfig.getInitParameter("scope");
		this.tokenURI = URI.create(filterConfig.getInitParameter("tokenURI"));
		this.usernameURI = URI.create(filterConfig.getInitParameter("usernameURI"));
		this.usernameJSONKey = filterConfig.getInitParameter("usernameJSONKey");
		this.clientSecret = new Secret(filterConfig.getInitParameter("clientSecret"));
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse resp = (HttpServletResponse) response;

		// OAuth 2.0のコールバック
		if (req.getRequestURI().equals("/oauth2/callback")) {
			try {
				doCallback(req, resp);
			} catch (IOException | ServletException e) {
				throw e;
			} catch (final Exception e) {
				throw new ServletException(e);
			}
			return;
		}

		// セッションにユーザーがあるならログイン済み
		final HttpSession session = req.getSession();
		final Object user = session.getAttribute("user");
		if (user != null) {
			chain.doFilter(request, response);
			return;
		}

		// 認可へ進む
		final State state = new State();
		final AuthorizationRequest authorizationRequest = new AuthorizationRequest.Builder(
				new ResponseType(ResponseType.Value.CODE), clientID)
						.scope(new Scope(scope))
						.state(state)
						.redirectionURI(callbackURI)
						.endpointURI(authzURI)
						.build();
		final URI requestURI = authorizationRequest.toURI();
		session.setAttribute(state.getValue(),
				req.getRequestURI()
						+ (req.getQueryString() != null ? "?" + req.getQueryString() : ""));
		resp.sendRedirect(requestURI.toString());
	}

	private void doCallback(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		final var authorizationResponse = handleAuthorization(req);
		if (authorizationResponse.isEmpty()) {
			resp.sendError(401);
			return;
		}

		final AuthorizationSuccessResponse response = authorizationResponse.get();

		final var requestedURI = extractRequestedURI(req, response.getState());
		if (requestedURI.isEmpty()) {
			resp.sendError(401);
			return;
		}

		final var accessTokenResponse = getAccessToken(response.getAuthorizationCode());
		if (accessTokenResponse.isEmpty()) {
			resp.sendError(401);
			return;
		}

		final var username = getUsername(accessTokenResponse.get().getTokens());
		if (username.isEmpty()) {
			resp.sendError(401);
			return;
		}

		req.getSession().setAttribute("user", username.get());

		resp.sendRedirect(requestedURI.get());
	}

	private Optional<AuthorizationSuccessResponse> handleAuthorization(HttpServletRequest req)
			throws Exception {
		final Map<String, List<String>> params = req.getParameterMap()
				.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, a -> List.of(a.getValue())));
		final URI redirectURI = URI.create(req.getRequestURI());
		final AuthorizationResponse response = AuthorizationResponse.parse(redirectURI, params);
		if (response.indicatesSuccess() == false) {
			return Optional.empty();
		}
		return Optional.of((AuthorizationSuccessResponse) response);
	}

	private Optional<String> extractRequestedURI(HttpServletRequest req, State state) {
		final HttpSession session = req.getSession();
		final String requestedUri = (String) session.getAttribute(state.getValue());
		return Optional.ofNullable(requestedUri);
	}

	private Optional<AccessTokenResponse> getAccessToken(AuthorizationCode code) throws Exception {
		final AuthorizationGrant codeGrant = new AuthorizationCodeGrant(code, callbackURI);

		final ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);

		final TokenRequest request = new TokenRequest(tokenURI, clientAuth, codeGrant);

		final HTTPRequest httpRequest = request.toHTTPRequest();
		httpRequest.setAccept("application/json");
		final HTTPResponse sent = httpRequest.send();
		final TokenResponse tokenResponse = TokenResponse.parse(sent);
		if (tokenResponse.indicatesSuccess() == false) {
			return Optional.empty();
		}

		return Optional.of((AccessTokenResponse) tokenResponse);
	}

	private Optional<String> getUsername(Tokens tokens) throws Exception {
		final HttpClient newHttpClient = HttpClient.newHttpClient();

		final HttpResponse<byte[]> httpResponse = newHttpClient.send(HttpRequest.newBuilder()
				.GET()
				.uri(usernameURI)
				.header("Accept", "application/json")
				.header("Authorization",
						"Bearer " + tokens.getAccessToken().getValue())
				.build(),
				HttpResponse.BodyHandlers.ofByteArray());

		if (httpResponse.statusCode() != 200) {
			return Optional.empty();
		}

		final JSONObject object = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE)
				.parse(httpResponse.body());

		return Optional.of(object.getAsString(usernameJSONKey));
	}
}
