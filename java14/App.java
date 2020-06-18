public class App {

    public static void main(String[] args) {
        new App().showCodeDetailsInExceptionMessages(null);
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

     String  patternMatch(Object o) {
         if (o instanceof String s) {
            return "string";
         }
         if (o instanceof Integer i) {
             return "int";
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

     Object[] record() {
        var hoge = new Hoge("hello", 123);
        var s = hoge.foo();
        var i = hoge.bar();
        return new Object[] { s, i };
    }
}

record Hoge(String foo, int bar) {
}

