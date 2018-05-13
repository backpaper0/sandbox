package parser.combinator.example.json;

import parser.combinator.ParseContext;
import parser.combinator.ParseException;
import parser.combinator.Parser;
import parser.combinator.ParserCombinator;

public class JsonParser extends ParserCombinator {

    public static void main(final String[] args) {
        final Parser parser = new JsonParser().parser();
        System.out.println(parser.parse("{}"));
        System.out.println(parser.parse("[]"));
        System.out.println(parser.parse("1"));
        System.out.println(parser.parse("2"));
        System.out.println(parser.parse("null"));
        System.out.println(parser.parse("true"));
        System.out.println(parser.parse("false"));
        System.out.println(parser.parse("\"\""));
        System.out.println(parser.parse("[[]]"));
        System.out.println(parser.parse("[{}]"));
        System.out.println(parser.parse("[null]"));
        System.out.println(parser.parse("[1]"));
        System.out.println(parser.parse("[true]"));
        System.out.println(parser.parse("[false]"));
        System.out.println(parser.parse("[[],[]]"));
        System.out.println(parser.parse("[{},{}]"));
        System.out.println(parser.parse("[null,null]"));
        System.out.println(parser.parse("[1,1]"));
        System.out.println(parser.parse("[true,true]"));
        System.out.println(parser.parse("[false,false]"));
        System.out.println(parser.parse("{\"v\":null}"));
        System.out.println(parser.parse("{\"v\":1}"));
        System.out.println(parser.parse("{\"v\":[]}"));
        System.out.println(parser.parse("{\"v\":{}}"));
        System.out.println(parser.parse("{\"v\":\"foo\"}"));
        System.out.println(parser.parse("{\"v\":true}"));
        System.out.println(parser.parse("{\"v\":false}"));
        System.out.println(parser.parse("{\"a\":[true,false,{\"bb\":\"c\"}]}"));
        System.out.println(
                parser.parse("{\"foo\":[\"hello\",12345,true,null],\"bar\":{},\"baz\":null}"));
    }

    /*
     * jvalue = jobject | jarray | jboolean
     *        | jnull | jstring | jnumber;
     * jobject = '{' 
     *   [jstring ':' jvalue {',' jstring ':' jvalue}]
     * '}';
     * jarray = '[' [jvalue {',' jvalue}] ']';
     * jboolean = 'true' | 'false';
     * jnull = 'null';
     * jstring = '"' ... '"';
     * jnumber = integer;
     */

    public Parser parser() {
        return and(jvalue(), eof());
    }

    Parser jvalue() {
        return or(jobject(), jarray(), jboolean(), jnull(), jstring(), jnumber());
    }

    Parser jobject() {
        return and(literal("{"),
                option(and(jstring(), literal(":"), lazy(this::jvalue),
                        repeat(and(literal(","), jstring(), literal(":"), lazy(this::jvalue))))),
                literal("}"));
    }

    Parser jarray() {
        return and(literal("["),
                option(and(lazy(this::jvalue), repeat(and(literal(","), lazy(this::jvalue))))),
                literal("]"));
    }

    Parser jboolean() {
        return or(literal("true"), literal("false"));
    }

    Parser jnull() {
        return literal("null");
    }

    Parser jstring() {
        return and(literal("\""), this::string, literal("\""));
    }

    Parser jnumber() {
        return this::integer;
    }

    Object string(final ParseContext context) {
        final StringBuilder buf = new StringBuilder();
        for (;;) {
            final char c = context.getChar();
            if (c == '"') {
                return buf.toString();
            }
            buf.append(c);
            context.consume();
        }
    }

    Object integer(final ParseContext context) {
        final char c0 = context.getChar();
        if (c0 == '0') {
            context.consume();
            return "0";
        } else if ('1' <= c0 && c0 <= '9') {
            final StringBuilder buf = new StringBuilder();
            buf.append(c0);
            context.consume();
            for (;;) {
                final char c = context.getChar();
                if ('0' <= c && c <= '9') {
                    buf.append(c);
                    context.consume();
                } else {
                    return buf.toString();
                }
            }
        }
        throw new ParseException(context, "not integer");
    }
}
