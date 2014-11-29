package irof;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

public class Irof extends ClassLoader implements Opcodes {

    public static void main(String[] args) throws Exception {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        String className = "162";
        cw.visit(V1_8, ACC_PUBLIC, className, null, "java/lang/Object",
                new String[] { "java/lang/Runnable" });

        Function<String, Method> toVoidNoArgMethod = name -> new Method(name,
                Type.VOID_TYPE, new Type[0]);

        Function<Method, GeneratorAdapter> toGeneratorAdapter = method -> new GeneratorAdapter(
                ACC_PUBLIC, method, null, null, cw);

        //コンストラクタを追加する。
        //Javaコードをjavacすると勝手に追加されるけどクラスファイルを書き出す場合は
        //心を込めて手動で追加する必要がある。
        //コンストラクタは<init>という名前でJava言語では記号はメソッド名に使えない
        //から他のメソッドと被ることはない。
        //また、コンストラクタの呼び出しは invokespecial というオペコードを使うが
        //GeneratorAdapterではinvokeConstructorというより分かりやすい名前になっている。
        //でもinvokespecialメソッド、めっちゃ探した(真顔)
        {
            Method constructor = toVoidNoArgMethod.apply("<init>");
            GeneratorAdapter ga = toGeneratorAdapter.apply(constructor);
            ga.loadThis();
            ga.invokeConstructor(Type.getType(Object.class), constructor);
            ga.returnValue();
            ga.endMethod();
        }

        //定義するメソッドの名前をファイルから読み込む。
        URI uri = Irof.class.getResource("/irof.txt").toURI();
        Path path = Paths.get(uri);
        List<String> lines = Files.readAllLines(path);
        lines.add("run"); //Runnable#run
        //顎の方から書き出したいのでリバースする。
        Collections.reverse(lines);

        //次のメソッドを呼び出すだけのメソッドを定義する。
        //最後に呼び出されたメソッドで例外を投げる。
        lines.stream().map(toVoidNoArgMethod).reduce((method1, method2) -> {
            GeneratorAdapter ga = toGeneratorAdapter.apply(method1);
            ga.loadThis();
            ga.invokeVirtual(Type.getType(className), method2);
            ga.returnValue();
            ga.endMethod();
            return method2;

        }).ifPresent(method -> {
            GeneratorAdapter ga = toGeneratorAdapter.apply(method);
            ga.throwException(Type.getType(RuntimeException.class), "");
            ga.endMethod();
        });

        cw.visitEnd();

        //作ったクラスをアレしてインスタンス化してrunして例外でアレ

        byte[] code = cw.toByteArray();

        Class<?> irofClass = new Irof().defineClass(className, code, 0,
                code.length);

        Runnable irof = (Runnable) irofClass.newInstance();

        irof.run();
    }
}
