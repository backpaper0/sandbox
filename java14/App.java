public class App {

    public static void main(String[] args) {
        var app = new App();

        System.out.println("[Switch expression]");
        System.out.println(app.switchExpression("foo"));
        System.out.println(app.switchExpression("bar"));
        System.out.println(app.switchExpression("baz"));
        System.out.println();

        System.out.println("[Pattern match]");
        System.out.println(app.patternMatch("hello"));
        System.out.println(app.patternMatch(123));
        System.out.println(app.patternMatch(true));
        System.out.println();

        System.out.println("[Text block]");
        System.out.println(app.textBlock());
        System.out.println();

        System.out.println("[Record]");
        var hoge1 = new Hoge("hello", 123);
        var hoge2 = new Hoge("hello", 123);
        var hoge3 = new Hoge("hello", 999);
        System.out.println(hoge1);
        System.out.println(hoge1.foo());
        System.out.println(hoge1.bar());
        System.out.println(hoge1.equals(hoge2));
        System.out.println(hoge1.equals(hoge3));
        System.out.println("Constructors");
        java.util.Arrays.stream(Hoge.class.getConstructors()).forEach(constructor -> {
            System.out.println("    " + constructor);
        });
        System.out.println("Methods");
        java.util.Arrays.stream(Hoge.class.getMethods()).forEach(method -> {
            System.out.println("    " + method);
        });
        System.out.println();

        System.out.println("[-XX:+ShowCodeDetailsInExceptionMessages]");
        app.showCodeDetailsInExceptionMessages(null);
    }

    String showCodeDetailsInExceptionMessages(Object o) {
        return o.toString();
    }

     int switchExpression(String s) {
        return switch (s) {
            case "foo" -> 1; 
            case "bar" -> 2; 
            default -> 0;
        };
    }

     String patternMatch(Object o) {
         if (o instanceof String s) {
            return "string: " + s;
         }
         if (o instanceof Integer i) {
             return "int: " + i;
         }
         return "-";
    }

     String textBlock() {
        return """
            foo
            bar
            baz
            """;
    }
}

record Hoge(String foo, int bar) {
}

