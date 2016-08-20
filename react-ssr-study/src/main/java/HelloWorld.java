import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        ScriptContext context = new SimpleScriptContext();
        HelloWorld hello = new HelloWorld(engine, context);
        hello.run();
    }

    final ScriptEngine engine;
    final ScriptContext context;

    public HelloWorld(ScriptEngine engine, ScriptContext context) {
        this.engine = engine;
        this.context = context;
    }

    void run() throws Exception {

        load("/META-INF/resources/webjars/react/15.3.0/dist/react.js");
        load("/META-INF/resources/webjars/react-dom/15.3.0/dist/react-dom-server.js");

        Object reactElement;
        try (Reader in = Files.newBufferedReader(Paths.get("hello.js"))) {
            reactElement = engine.eval(in, context);
        }

        Object result = renderToString(reactElement);

        System.out.println(result);
    }

    void load(String script) throws Exception {
        try (Reader in = new InputStreamReader(
                HelloWorld.class.getResourceAsStream(script),
                StandardCharsets.UTF_8)) {
            engine.eval(in, context);
        }
    }

    Object renderToString(Object reactElement) throws Exception {
        Object reactDOMServer = engine.eval("ReactDOMServer", context);
        return ((Invocable) engine).invokeMethod(reactDOMServer, "renderToString", reactElement);
    }
}
