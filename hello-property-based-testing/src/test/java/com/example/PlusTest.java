package com.example;

import org.junit.jupiter.api.Test;
import org.quicktheories.WithQuickTheories;

public class PlusTest implements WithQuickTheories {

    @Test
    void plus() throws Exception {
        qt().forAll(integers().between(1, 100), integers().between(1, 100))
                .check((a, b) -> Plus.plus(a, b) >= 0);
    }
}
