package com.example;

import static spark.Spark.*;

public class HelloWorld {
    public static void main(final String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}