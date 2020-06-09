package com.example;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("")
public class App extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		return Set.of(Api.class, TryFinally.class, IOExceptionMapper.class);
	}
}
