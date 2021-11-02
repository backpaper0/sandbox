package com.example.foo;

public class Foo {
	public static void main(String[] args) {
		System.out.println("Foo, " + new com.example.bar.Bar());
		System.out.println("Foo, " + new com.example.bar.internal.InternalBar());
	}
}
