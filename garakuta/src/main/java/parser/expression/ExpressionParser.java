package parser.expression;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class ExpressionParser extends Parser {

    public static void main(final String[] args) {
        System.out.println(new ExpressionParser("0").parse());
        System.out.println(new ExpressionParser("1").parse());
        System.out.println(new ExpressionParser("234567890").parse());
        System.out.println(new ExpressionParser("1+2*3-4/5").parse());
        System.out.println(new ExpressionParser("(1+2)*(3-4)/5").parse());
    }

    public ExpressionParser(final String input) {
        super(input);
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

    public ExpressionNode parse() {
        final ExpressionNode node = expression();
        match(EOF);
        return node;
    }

    private ExpressionNode expression() {
        return additive();
    }

    private ExpressionNode additive() {
        return additive(multitive());
    }

    private ExpressionNode additive(final ExpressionNode node) {
        final Stream<Supplier<ExpressionNode>> stream = Stream.of(
                () -> {
                    match('+');
                    return ExpressionNode.add(node, multitive());
                },
                () -> {
                    match('-');
                    return ExpressionNode.sub(node, multitive());
                });
        return stream.map(this::tryParse)
                .flatMap(a -> a.map(Stream::of).orElseGet(Stream::empty))
                .map(this::additive)
                .findFirst()
                .orElse(node);
    }

    private ExpressionNode multitive() {
        return multitive(primary());
    }

    private ExpressionNode multitive(final ExpressionNode node) {
        final Stream<Supplier<ExpressionNode>> stream = Stream.of(
                () -> {
                    match('*');
                    return ExpressionNode.mul(node, primary());
                },
                () -> {
                    match('/');
                    return ExpressionNode.div(node, primary());
                });
        return stream.map(this::tryParse)
                .flatMap(a -> a.map(Stream::of).orElseGet(Stream::empty))
                .map(this::multitive)
                .findFirst()
                .orElse(node);
    }

    private ExpressionNode primary() {
        return tryParse(() -> {
            match('(');
            final ExpressionNode node = expression();
            match(')');
            return node;
        }).orElseGet(this::integer);
    }

    private ExpressionNode integer() {
        final int value = tryParse(() -> zero())
                .orElseGet(() -> digitRest(digitFirst()));
        return ExpressionNode.val(value);
    }

    private int digitRest(final int value) {
        return tryParse(() -> value * 10 + digitRest())
                .map(this::digitRest)
                .orElse(value);
    }

    private int zero() {
        match('0');
        return 0;
    }

    private int digitFirst() {
        final char c = getChar();
        if ('1' <= c && c <= '9') {
            consume();
            return c - '0';
        }
        throw new ParseException();
    }

    private int digitRest() {
        final char c = getChar();
        if ('0' <= c && c <= '9') {
            consume();
            return c - '0';
        }
        throw new ParseException();
    }
}
