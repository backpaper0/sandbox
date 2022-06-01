package com.example;

import java.util.Locale;
import java.util.TimeZone;

public class App {

	public static void main(String[] args) {
		System.out.println(getDefaultLocale());
		System.out.println(getDefaultTimeZone());
	}

	static Locale getDefaultLocale() {
		return Locale.getDefault();
	}

	static TimeZone getDefaultTimeZone() {
		return TimeZone.getDefault();
	}
}
