import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Interceptor1 {

    public static void main(final String[] args) {

        final EnhancedHoge hoge = new EnhancedHoge();

        hoge.addInterceptor(context -> {
            try {
                System.out.println("begin 1");
                return context.proceed();
            } finally {
                System.out.println("end 1");
            }
        });

        hoge.addInterceptor(context -> {
            try {
                System.out.println("begin 2");
                return context.proceed();
            } finally {
                System.out.println("end 2");
            }
        });

        final String s = hoge.echo("hello");

        System.out.printf("result: %s%n", s);
    }

    interface Interceptor {
        Object invoke(InterceptorContext context);
    }

    interface InterceptorContext {
        Object proceed();
    }

    static class Hoge {
        public String echo(final String s) {
            System.out.println(s);
            return s;
        }
    }

    static class EnhancedHoge extends Hoge {
        private final List<Interceptor> interceptors = new ArrayList<>();

        public boolean addInterceptor(final Interceptor interceptor) {
            return interceptors.add(interceptor);
        }

        @Override
        public String echo(final String s) {
            final Iterator<Interceptor> it = interceptors.iterator();
            //この匿名クラスをなくしたいがthisを参照しているので
            //ラムダ式にできない！！！
            //Interceptor2 へ続く。
            return (String) new InterceptorContext() {
                @Override
                public Object proceed() {
                    if (it.hasNext()) {
                        return it.next().invoke(this);
                    }
                    return EnhancedHoge.super.echo(s);
                }
            }.proceed();
        }
    }
}
