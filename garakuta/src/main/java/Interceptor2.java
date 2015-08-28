import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Interceptor2 {

    public static void main(String[] args) {

        EnhancedHoge hoge = new EnhancedHoge();

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

        String s = hoge.echo("hello");

        System.out.printf("result: %s%n", s);
    }

    interface Interceptor {
        Object invoke(InterceptorContext context);
    }

    interface InterceptorContext {
        Object proceed();
    }

    interface RecursiveInterceptorContext extends InterceptorContext {
        Object proceed(InterceptorContext context);

        @Override
        default Object proceed() {
            return proceed(this);
        }
    }

    static class Hoge {
        public String echo(String s) {
            System.out.println(s);
            return s;
        }
    }

    static class EnhancedHoge extends Hoge {
        private final List<Interceptor> interceptors = new ArrayList<>();

        public boolean addInterceptor(Interceptor interceptor) {
            return interceptors.add(interceptor);
        }

        @Override
        public String echo(String s) {
            Iterator<Interceptor> it = interceptors.iterator();
            //補助的な関数型インターフェースを噛ませることで
            //匿名クラスがなくなった。
            RecursiveInterceptorContext context = self -> {
                if (it.hasNext()) {
                    return it.next().invoke(self);
                }
                return EnhancedHoge.super.echo(s);
            };
            return (String) context.proceed();
        }
    }
}
