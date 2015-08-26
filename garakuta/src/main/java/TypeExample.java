
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class TypeExample {

    public static void main(String[] args) {
        //Class
        //GenericArrayType
        //ParameterizedType
        //TypeVariable<D>
        //WildcardType
        new TypeExample().example(null, null, null, null, null, null, null);
    }

    <T, U extends T> void example(
            //Class
            String a,
            //GenericArrayType
            T[] b,
            //ParameterizedType < TypeVariable >
            List<T> c,
            //ParameterizedType < WildcardType >
            List<?> d,
            //ParameterizedType < GenericArrayType >
            List<T[]> e,
            //ParameterizedType < WildcardType >
            List<? extends T> f,
            //ParameterizedType < TypeVariable >
            List<U> g) {
        Method method = Arrays.stream(getClass().getDeclaredMethods())
                .filter(x -> x.getName().equals("example")).findAny().get();

        Type[] pt = method.getGenericParameterTypes();

        BiConsumer<Type, Class<?>> fn = (t, u) -> {
            if (u.isAssignableFrom(t.getClass()) == false) {
                throw new RuntimeException();
            }
            System.out.printf("[ %s ] is type of [ %s ]%n", t,
                    u.getSimpleName());
        };

        fn.accept(pt[0], Class.class);
        fn.accept(pt[1], GenericArrayType.class);
        fn.accept(pt[2], ParameterizedType.class);
        fn.accept(pt[3], ParameterizedType.class);
        fn.accept(pt[4], ParameterizedType.class);
        fn.accept(pt[5], ParameterizedType.class);
        fn.accept(pt[6], ParameterizedType.class);
        fn.accept(((ParameterizedType) pt[2]).getActualTypeArguments()[0],
                TypeVariable.class);
        fn.accept(((ParameterizedType) pt[3]).getActualTypeArguments()[0],
                WildcardType.class);
        fn.accept(((ParameterizedType) pt[4]).getActualTypeArguments()[0],
                GenericArrayType.class);
        fn.accept(((ParameterizedType) pt[5]).getActualTypeArguments()[0],
                WildcardType.class);
        fn.accept(((ParameterizedType) pt[6]).getActualTypeArguments()[0],
                TypeVariable.class);
    }
}
