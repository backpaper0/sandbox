package sample;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.interceptor.InterceptorBinding;

/**
 * インターセプターを適用するためのアノテーションです。
 *
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Sample {
}
