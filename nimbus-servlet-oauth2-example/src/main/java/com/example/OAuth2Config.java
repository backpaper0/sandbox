package com.example;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class OAuth2Config implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final FilterRegistration.Dynamic reg = sce.getServletContext()
				.addFilter("OAuth 2.0", OAuth2Filter.class);
		reg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		reg.setInitParameter("authzURI", "https://github.com/login/oauth/authorize");
		reg.setInitParameter("clientID", System.getenv("OAUTH2_CLIENT_ID"));
		reg.setInitParameter("scope", "user");
		reg.setInitParameter("tokenURI", "https://github.com/login/oauth/access_token");
		reg.setInitParameter("usernameURI", "https://api.github.com/user");
		reg.setInitParameter("usernameJSONKey", "login");
		reg.setInitParameter("clientSecret", System.getenv("OAUTH2_CLIENT_SECRET"));
	}
}
