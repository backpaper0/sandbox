package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Foobar {

    private String foo;
    private String bar;

    public String foobar() {
        return foo + bar;
    }
}
