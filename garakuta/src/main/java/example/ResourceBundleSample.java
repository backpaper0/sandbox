package example;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import com.google.common.base.Objects;

public class ResourceBundleSample {

    private static final String KEY = "HELLO";

    public static void main(final String[] args) {
        final String baseName = "example.ResourceBundleSample$ResourceBundleImpl";

        final List<Locale> locales = Arrays.asList(Locale.JAPANESE, Locale.JAPAN, Locale.ENGLISH);

        for (final Locale locale : locales) {
            final ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
            System.out.printf("%1$s (%2$s)%n", bundle.getString(KEY), locale);
        }
    }

    private static abstract class ResourceBundleSkeleton extends ResourceBundle {
        protected abstract Object handleGetObjectInternal();

        @Override
        protected Object handleGetObject(final String key) {
            return Objects.equal(key, KEY) ? handleGetObjectInternal() : null;
        }

        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(Collections.singleton(KEY));
        }
    }

    public static class ResourceBundleImpl_en extends ResourceBundleSkeleton {
        @Override
        protected Object handleGetObjectInternal() {
            return "Hello, world!";
        }
    }

    public static class ResourceBundleImpl_ja_JP extends ResourceBundleSkeleton {
        @Override
        protected Object handleGetObjectInternal() {
            return "こんにちは、世界！";
        }
    }
}
