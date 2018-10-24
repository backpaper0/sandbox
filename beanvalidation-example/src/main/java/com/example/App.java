package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;

public class App {

    public static void main(final String[] args) {

        final List<Bar> bars = new ArrayList<>();
        bars.add(new Bar("a"));
        bars.add(new Bar(null));
        bars.add(new Bar("b"));
        bars.add(new Bar(null));
        bars.add(new Bar("c"));
        final Foo foo = new Foo(null, bars);

        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<Foo>> cvs = validator.validate(foo);

        for (final ConstraintViolation<Foo> cv : cvs) {
            if (cv.getLeafBean() instanceof Bar) {
                final Path.Node node = cv.getPropertyPath().iterator().next();
                System.out.printf("[%s]%s%n", node, cv.getMessage());
            } else {
                System.out.println(cv.getMessage());
            }
        }
    }
}
