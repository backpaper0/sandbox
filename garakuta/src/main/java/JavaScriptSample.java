import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JavaScriptSample {

    public static void main(String[] args) throws Exception {

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");

        engine.eval("function sum(a, b) { return a + b; }");

        int a = 1;
        int b = 2;
        Object c = ((Invocable) engine).invokeFunction("sum", a, b);
        System.out.println(c);
        System.out.println(c.getClass());

        List<?> list = (List<?>) engine.eval("var list = new (Java.type('java.util.ArrayList'));"
                + "list.add('hello');"
                + "list.add(123);"
                + "list.add(true);"
                + "list;");
        list.forEach(System.out::println);
    }
}
