package org.sample;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;

public class MyBenchmark {

    @Benchmark
    public List<String> fizzbuzz() {
        return IntStream.rangeClosed(1, 100)
                .mapToObj(i -> {
                    if (i % 15 == 0) {
                        return "FizzBuzz";
                    } else if (i % 3 == 0) {
                        return "Fizz";
                    } else if (i % 5 == 0) {
                        return "Buzz";
                    }
                    return String.valueOf(i);
                })
                .collect(Collectors.toList());
    }
}
