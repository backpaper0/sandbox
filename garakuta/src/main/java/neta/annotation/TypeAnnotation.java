package neta.annotation;

import java.io.Serializable;
import java.io.UncheckedIOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Hoge
public class TypeAnnotation<@Hoge A, @Hoge B> extends @Hoge ArrayList<@Hoge String>
        implements @Hoge Serializable {

    <@Hoge C> @Hoge Set<@Hoge String> method(@Fuga TypeAnnotation<@Fuga A, @Fuga B>this,
            @Hoge final Map<@Hoge String, @Hoge List<@Hoge String>> map)
                    throws @Hoge IllegalArgumentException, @Hoge UncheckedIOException {
        @Hoge
        final
        List<@Hoge String> list = new @Hoge ArrayList<>();
        Arrays.<@Hoge Number> asList(1, 2, 3);
        return null;
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@interface Hoge {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@interface Fuga {
}