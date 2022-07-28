package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.sub.Bar;

public class Foo {

	private Integer value1;
	private String value2;

	public void getValue1() {
		System.out.println(value1);
	}

	public void getValue2() {
		System.out.println(value2);
	}

	public String getList() {
		List<String> list = new ArrayList<>();
		return list.get(0);
	}

	public Bar newBar() {
		return new Bar();
	}
}
