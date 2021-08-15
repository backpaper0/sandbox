package com.example;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SecondListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		throw new MyException(getClass().getName());
	}
}
