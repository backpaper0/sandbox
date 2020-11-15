package com.example;

public class Main {
    public static void main(String args[]) {
        System.out.println("Hello World!");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("**** Environment Variables ****");
        System.getenv().entrySet().stream().sorted(java.util.Comparator.comparing(java.util.Map.Entry::getKey)).forEach(System.out::println);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("**** System Properties ****");
        System.getProperties().entrySet().stream().sorted(java.util.Comparator.comparing(a -> a.getKey().toString())).forEach(System.out::println);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Bye!");
    }
}

