package example;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ExceptionChain extends TypeSafeMatcher<Throwable> {

    private final Class<? extends Throwable>[] classes;

    public ExceptionChain(Class<? extends Throwable>[] classes) {
        this.classes = classes;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(Arrays.stream(classes).map(Class::getName)
                .collect(Collectors.joining(", ")));
    }

    @Override
    protected boolean matchesSafely(Throwable item) {
        Throwable t = item;
        for (Class<? extends Throwable> clazz : classes) {
            if (t == null || t.getClass().equals(clazz) == false) {
                return false;
            }
            t = t.getCause();
        }
        return true;
    }

    @SafeVarargs
    public static Matcher<?> exceptionChain(
            Class<? extends Throwable>... classes) {
        return new ExceptionChain(classes);
    }
}
