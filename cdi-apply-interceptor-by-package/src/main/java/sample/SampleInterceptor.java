package sample;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * 戻り値の前後に*を付けて返すインターセプターです。
 *
 */
@Sample
@Interceptor
public class SampleInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        return "*" + context.proceed() + "*";
    }
}
