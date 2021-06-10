package com.example;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class App {

	public static void main(String[] args) {
		Runtime runtime = Runtime.getRuntime();

		Items items = new Items();
		items.add("availableProcessors", runtime.availableProcessors(), "");
		items.add("freeMemory", toMB(runtime.freeMemory()), "MB");
		items.add("totalMemory", toMB(runtime.totalMemory()), "MB");
		items.add("maxMemory", toMB(runtime.maxMemory()), "MB");

		StringWriter s = new StringWriter();
		PrintWriter out = new PrintWriter(s);
		items.writeTo(out);
		System.out.println(s);
	}

	private static long toMB(long value) {
		return value / 1024 / 1024;
	}

	static class Item {
		String name;
		String value;
		String unit;

		public Item(String name, String value, String unit) {
			this.name = name;
			this.value = value;
			this.unit = unit;
		}

		void writeTo(PrintWriter out, int nameMaxLength, int valueMaxLength) {
			IntStream.range(0, nameMaxLength - name.length()).forEach(i -> out.print(' '));
			out.print(name);
			out.print(": ");
			IntStream.range(0, valueMaxLength - value.length())
					.forEach(i -> out.print(' '));
			out.print(value);
			if (unit != null) {
				out.print(' ');
				out.print(unit);
			}
			out.println();
		}
	}

	static class Items {
		List<Item> items = new ArrayList<>();
		int nameMaxLength;
		int valueMaxLength;

		void add(String name, Number value, String unit) {
			String valueAsString = String.format("%,d", value);
			items.add(new Item(name, valueAsString, unit));
			nameMaxLength = Math.max(nameMaxLength, name.length());
			valueMaxLength = Math.max(valueMaxLength, valueAsString.length());
		}

		void writeTo(PrintWriter out) {
			for (Item item : items) {
				item.writeTo(out, nameMaxLength, valueMaxLength);
			}
		}
	}
}
