package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

public class AppTest {

	@Test
	void getDefaultLocale() {
		Locale locale = App.getDefaultLocale();
		assertEquals(Locale.JAPAN, locale);
	}

	@Test
	void getDefaultTimeZone() {
		TimeZone timeZone = App.getDefaultTimeZone();
		assertEquals(TimeZone.getTimeZone("Asia/Tokyo"), timeZone);
	}
}
