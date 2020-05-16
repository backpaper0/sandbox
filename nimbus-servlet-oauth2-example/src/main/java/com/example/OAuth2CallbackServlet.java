package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.AuthorizationResponse;
import com.nimbusds.oauth2.sdk.AuthorizationSuccessResponse;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@WebServlet(urlPatterns = "/oauth2/callback")
public class OAuth2CallbackServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final Map<String, List<String>> params = req.getParameterMap()
				.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, a -> List.of(a.getValue())));
		final URI redirectURI = URI.create(req.getRequestURI());
		try {
			final AuthorizationResponse authorizationResponse = AuthorizationResponse
					.parse(redirectURI, params);
			if (authorizationResponse.indicatesSuccess() == false) {
				resp.sendError(401);
				return;
			}
			final AuthorizationSuccessResponse response = (AuthorizationSuccessResponse) authorizationResponse;

			final State state = response.getState();
			final HttpSession session = req.getSession();
			final String requestedUri = (String) session.getAttribute(state.getValue());
			if (requestedUri == null) {
				resp.sendError(401);
				return;
			}

			final AuthorizationCode code = response.getAuthorizationCode();
			final URI callback = URI.create("http://localhost:8080/oauth2/callback");
			final AuthorizationGrant codeGrant = new AuthorizationCodeGrant(code, callback);

			final ClientID clientID = new ClientID(System.getenv("OAUTH2_CLIENT_ID"));
			final Secret clientSecret = new Secret(System.getenv("OAUTH2_CLIENT_SECRET"));
			final ClientAuthentication clientAuth = new ClientSecretBasic(
					clientID, clientSecret);

			final URI tokenEndpoint = URI.create("https://github.com/login/oauth/access_token");

			final TokenRequest request = new TokenRequest(tokenEndpoint, clientAuth, codeGrant);

			final HTTPRequest httpRequest = request.toHTTPRequest();
			httpRequest.setAccept("application/json");
			final HTTPResponse sent = httpRequest.send();
			System.out.println(sent.getHeaderValue("Content-Type"));
			final TokenResponse tokenResponse = TokenResponse.parse(sent);
			if (tokenResponse.indicatesSuccess() == false) {
				resp.sendError(401);
				return;
			}

			final AccessTokenResponse accessTokenResponse = (AccessTokenResponse) tokenResponse;

			final HttpClient newHttpClient = HttpClient.newHttpClient();

			final HttpResponse<byte[]> httpResponse = newHttpClient.send(HttpRequest.newBuilder()
					.GET()
					.uri(URI.create("https://api.github.com/user"))
					.header("Accept", "application/json")
					.header("Authorization",
							"Bearer "
									+ accessTokenResponse.getTokens().getAccessToken().getValue())
					.build(),
					HttpResponse.BodyHandlers.ofByteArray());

			if (httpResponse.statusCode() != 200) {
				resp.sendError(401);
				return;
			}

			final JSONObject object = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE)
					.parse(httpResponse.body());

			session.setAttribute("user", object.getAsString("login"));

			resp.sendRedirect(requestedUri);

		} catch (final ParseException | InterruptedException
				| net.minidev.json.parser.ParseException e) {
			throw new IOException(e);
		}
	}
}
