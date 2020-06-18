package com.example;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

@HandlesTypes({ Foo.class })
public class Initializer implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		final Set<Class<?>> fooClasses = new HashSet<Class<?>>();
		for (final Class<?> clazz : c) {
			if (clazz.isAnnotationPresent(Foo.class)) {
				fooClasses.add(clazz);
			}
		}
		ctx.setAttribute(Foo.class.getName(), fooClasses);
	}
}
