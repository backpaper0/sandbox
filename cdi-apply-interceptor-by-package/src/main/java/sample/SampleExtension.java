package sample;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.util.AnnotationLiteral;

/**
 * sample.fooパッケージ以下のクラスにSampleInterceptorを適用するExtensionです。
 *
 */
public class SampleExtension implements Extension {

    public <T> void handle(@Observes ProcessAnnotatedType<T> event) {
        AnnotatedType<T> original = event.getAnnotatedType();

        //特定のパッケージ以下のクラスでなければ処理しない
        if (original.getJavaClass().getName()
                .startsWith("sample.foo.") == false) {
            return;
        }

        AnnotatedType<T> wrapped = new AnnotatedTypeWrapper<>(original);
        event.setAnnotatedType(wrapped);
    }

    static class AnnotatedTypeWrapper<X> implements AnnotatedType<X> {
        private final AnnotatedType<X> type;
        private final Set<Annotation> annotations;
        private final Sample sample;

        public AnnotatedTypeWrapper(AnnotatedType<X> type) {
            this.type = type;

            //Sampleアノテーションのインスタンス
            this.sample = new SampleImpl();

            //元から取得できるアノテーションにSampleアノテーションを
            //追加したSetを用意する。
            Set<Annotation> annotations = new HashSet<>(
                    type.getAnnotations().size() + 1);
            annotations.addAll(type.getAnnotations());
            annotations.add(sample);
            this.annotations = Collections.unmodifiableSet(annotations);
        }

        @Override
        public Class<X> getJavaClass() {
            return type.getJavaClass();
        }

        @Override
        public Type getBaseType() {
            return type.getBaseType();
        }

        @Override
        public Set<AnnotatedConstructor<X>> getConstructors() {
            return type.getConstructors();
        }

        @Override
        public Set<Type> getTypeClosure() {
            return type.getTypeClosure();
        }

        @Override
        public Set<AnnotatedMethod<? super X>> getMethods() {
            return type.getMethods();
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
            if (annotationType == Sample.class) {
                return (T) sample;
            }
            return type.getAnnotation(annotationType);
        }

        @Override
        public Set<AnnotatedField<? super X>> getFields() {
            return type.getFields();
        }

        @Override
        public Set<Annotation> getAnnotations() {
            return annotations;
        }

        @Override
        public boolean isAnnotationPresent(
                Class<? extends Annotation> annotationType) {
            return type.isAnnotationPresent(annotationType)
                    || annotationType == Sample.class;
        }
    }

    /**
     * Sampleアノテーションのインスタンスを生成するためのクラスです。
     * 
     * AnnotationLiteralではgetAnnotationTypeやequals、hashCodeといった
     * メソッドがリフレクションで提供されています。
     * 自前で実装する事も可能ですが、AnnotationLiteralを継承しておくのが
     * 楽だと思います。
     */
    static class SampleImpl extends AnnotationLiteral<Sample>
            implements Sample {
    }
}
