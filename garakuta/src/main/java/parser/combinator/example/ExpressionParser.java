package parser.combinator.example;

import parser.combinator.ParseException;
import parser.combinator.Parser;
import parser.combinator.ParserCombinator;

public class ExpressionParser extends ParserCombinator {

    public static void main(final String[] args) {
        final Parser parser = new ExpressionParser().parser();
        System.out.println(parser.parse("0"));
        System.out.println(parser.parse("1"));
        System.out.println(parser.parse("2345"));
        System.out.println(parser.parse("12+34"));
        System.out.println(parser.parse("1+2*3-4/5"));
        System.out.println(parser.parse("(1+2)*(3456-789)"));

        try {
            parser.parse("12 34");
        } catch (final ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            parser.parse("abc");
        } catch (final ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * expression = additive;
     * additive = multitive 
     *            {'+' multitive | '-' multitive};
     * multitive = primary
     *            {'*' primary | '/' primary};
     * primary = '(' expression ')' | integer;
     * 
     * // {e} はeの0回以上の繰り返し
     * integer = zero | digitFirst {digitRest}; 
     * zero = '0';
     * digitFirst = '1' | '2' | '3'| '4' | '5' | '6' 
     *            | '7' | '8' | '9';
     * digitRest = '0' | '1' | '2' | '3'| '4' | '5' | '6' 
     *            | '7' | '8' | '9';
     * 
     */

    public Parser parser() {
        return and(expression(), eof()).to(Converters.removeEof());
    }

    Parser expression() {
        return additive();
    }

    Parser additive() {
        return and(multitive(),
                repeat(or(and(literal("+"), multitive()), and(literal("-"), multitive()))))
                        .to(Converters.operation());
    }

    Parser multitive() {
        return and(primary(),
                repeat(or(and(literal("*"), primary()), and(literal("/"), primary()))))
                        .to(Converters.operation());
    }

    Parser primary() {
        return or(and(literal("("), lazy(this::expression), literal(")"))
                .to(Converters.removeParen()), integer());
    }

    Parser integer() {
        return or(zero(), and(digitFirst(), repeat(digitRest())).to(Converters.integer()));
    }

    Parser zero() {
        return literal("0").to(Converters.zero());
    }

    Parser digitFirst() {
        return or(literal("1"), literal("2"), literal("3"), literal("4"), literal("5"),
                literal("6"), literal("7"), literal("8"), literal("9"))
                        .to(Converters.digit());
    }

    Parser digitRest() {
        return or(literal("0"), literal("1"), literal("2"), literal("3"), literal("4"),
                literal("5"), literal("6"), literal("7"), literal("8"), literal("9"))
                        .to(Converters.digit());
    }
}
