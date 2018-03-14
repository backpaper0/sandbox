package org.sample;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MyBenchmarkTest {

    @Test
    void fizzbuzz() {
        final MyBenchmark benchmark = new MyBenchmark();
        final List<String> list = benchmark.fizzbuzz();
        Assertions.assertAll(() -> {
            Assertions.assertEquals(100, list.size());
            Assertions.assertEquals("1", list.get(0));
            Assertions.assertEquals("2", list.get(1));
            Assertions.assertEquals("Fizz", list.get(2));
            Assertions.assertEquals("4", list.get(3));
            Assertions.assertEquals("Buzz", list.get(4));
            Assertions.assertEquals("Fizz", list.get(5));
            Assertions.assertEquals("7", list.get(6));
            Assertions.assertEquals("8", list.get(7));
            Assertions.assertEquals("Fizz", list.get(8));
            Assertions.assertEquals("Buzz", list.get(9));
            Assertions.assertEquals("11", list.get(10));
            Assertions.assertEquals("Fizz", list.get(11));
            Assertions.assertEquals("13", list.get(12));
            Assertions.assertEquals("14", list.get(13));
            Assertions.assertEquals("FizzBuzz", list.get(14));
        });
    }
}
