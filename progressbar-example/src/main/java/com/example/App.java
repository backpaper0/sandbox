package com.example;

import java.util.stream.IntStream;

import me.tongfei.progressbar.ProgressBar;

public class App {

    public static void main(String[] args) throws InterruptedException {
        var size = 100;

        try (var pb = new ProgressBar("Example-1", size)) {
            for (int i = 0; i < size; i++) {
                sleep();
                pb.step();
            }
        }

        try (var is = IntStream.range(0, size);
                var stream = ProgressBar.wrap(is, "Example-2")) {
            stream.forEach(i -> {
                sleep();
            });
        }
    }

    static void sleep() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException ignore) {
        }
    }
}
