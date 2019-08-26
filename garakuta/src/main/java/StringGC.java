import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

public class StringGC {

    String name;

    Object instance;

    WeakReference<?> ref;

    public StringGC(String name, Object instance, ReferenceQueue<Object> q) {
        this.name = name;
        this.instance = instance;

        //弱参照オブジェクトを作って参照キューに突っ込まれたか
        //どうかでGCされたかどうかを確認する
        this.ref = new WeakReference<>(instance, q);
    }

    /**
     * 参照(強参照)をなくす
     */
    public void removeReference() {
        instance = null;
    }

    /**
     * GCされたかどうかを出力する関数
     */
    public void print() {
        System.out.printf("%s: GC%s%n", name, ref.isEnqueued()
                ? "されたよ╭( ・ㅂ・)و ̑̑ ｸﾞｯ ! " : "されてないよ");
    }

    public static void main(String[] args) throws Exception {

        ReferenceQueue<Object> q = new ReferenceQueue<>();

        List<StringGC> list = new ArrayList<>();

        list.add(new StringGC("リテラル", "aaa", q));
        list.add(new StringGC("newしたやつ", new String("bbb"), q));
        list.add(new StringGC("internしたやつ", new String(new char[] { 'c', 'c',
                'c' }).intern(), q));

        //クラスローダーを破棄したらリテラルもGCされるのか知りたい
        MyClassLoader loader = new MyClassLoader();
        Class<?> clazz = Class.forName(StringGC.class.getName() + "$Literal",
                false, loader);
        assert clazz.getClassLoader() == loader;
        Field field = clazz.getDeclaredField("literal");
        list.add(new StringGC("リテラルその2", clazz.getDeclaredField("literal").get(null), q));
        list.add(new StringGC("リテラルその2のクラスローダー", loader, q));
        loader = null;
        list.add(new StringGC("リテラルその2のクラス", clazz, q));
        clazz = null;
        list.add(new StringGC("リテラルその2のフィールド", field, q));
        field = null;

        //とりまGCされたか確認する
        list.forEach(StringGC::print);

        //参照(強参照)をなくす
        list.forEach(StringGC::removeReference);

        //空きメモリが増えなくなるなるまでGCしまくる
        LongSupplier f = () -> Runtime.getRuntime().freeMemory();
        long freeMemory = f.getAsLong();
        do {
            System.gc();
            System.out.println("GCしたよ");
        } while (freeMemory < (freeMemory = f.getAsLong()));

        //GCされたか確認する
        list.forEach(StringGC::print);
    }

    public static class Literal {

        public static String literal = "クラスをアンロードしたらリテラルどうなるの？";
    }

    public static class MyClassLoader extends ClassLoader {

        @Override
        protected Class<?> loadClass(String name, boolean resolve)
                throws ClassNotFoundException {
            if (name.equals(StringGC.class.getName() + "$Literal") == false) {
                return super.loadClass(name, resolve);
            }
            try {
                URL url = getResource(name.replace('.', '/') + ".class");
                URLConnection conn = url.openConnection();
                try (InputStream in = url.openStream()) {
                    byte[] b = new byte[conn.getContentLength()];
                    in.read(b);
                    Class<?> c = defineClass(name, b, 0, b.length);
                    if (resolve) {
                        resolveClass(c);
                    }
                    return c;
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
