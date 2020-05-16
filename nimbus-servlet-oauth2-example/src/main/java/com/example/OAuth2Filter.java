package com.example;

import java.io.IOException;
import java.net.URI;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;

@WebFilter(urlPatterns = "/*")
public class OAuth2Filter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse resp = (HttpServletResponse) response;

		// OAuth 2.0関係のURLはスルー
		if (req.getRequestURI().startsWith("/oauth2/")) {
			chain.doFilter(request, response);
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
		final URI authzEndpoint = URI.create("https://github.com/login/oauth/authorize");
		final ClientID clientID = new ClientID(System.getenv("OAUTH2_CLIENT_ID"));
		final Scope scope = new Scope("user");
		final URI callback = URI.create("http://localhost:8080/oauth2/callback");
		final State state = new State();
		final AuthorizationRequest authorizationRequest = new AuthorizationRequest.Builder(
				new ResponseType(ResponseType.Value.CODE), clientID)
						.scope(scope)
						.state(state)
						.redirectionURI(callback)
						.endpointURI(authzEndpoint)
						.build();
		final URI requestURI = authorizationRequest.toURI();
		session.setAttribute(state.getValue(),
				req.getRequestURI() + (req.getQueryString() != null ? req.getQueryString() : ""));
		resp.sendRedirect(requestURI.toString());
	}
}
