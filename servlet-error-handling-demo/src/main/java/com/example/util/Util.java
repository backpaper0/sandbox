package com.example.util;

import java.net.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Util {

	private static final String HANDLING_NAME_KEY = "X-Handling-Name";
	private static final String HANDLING_COUNT_KEY = "X-Handling-Count";

	public static void ready(HttpServletRequest req, HttpServletResponse resp, String name) {
		resp.setHeader(HANDLING_NAME_KEY, "Ready: " + name);
		int count = 0;
		resp.setIntHeader(HANDLING_COUNT_KEY, count);
		req.setAttribute(HANDLING_COUNT_KEY, count);
	}

	public static void handle(HttpServletRequest req, HttpServletResponse resp, String name) {
		resp.setHeader(HANDLING_NAME_KEY, "Handling: " + name);
		int count = (int) req.getAttribute(HANDLING_COUNT_KEY);
		count++;
		resp.setIntHeader(HANDLING_COUNT_KEY, count);
		req.setAttribute(HANDLING_COUNT_KEY, count);
	}

	public static String getHandlingName(HttpResponse<?> response) {
		return response.headers().firstValue(HANDLING_NAME_KEY).orElse("<None>");
	}

	public static int getHandlingCount(HttpResponse<?> response) {
		return (int) response.headers().firstValueAsLong(HANDLING_COUNT_KEY).orElse(-1);
	}
}
