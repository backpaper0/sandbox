package parser.expression;

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
        ExpressionNode node = multitive();
        for (;;) {
            final int savePoint1 = getPosition();
            try {
                match('+');
                node = ExpressionNode.add(node, multitive());
            } catch (final ParseException e1) {
                setPosition(savePoint1);
                final int savePoint2 = getPosition();
                try {
                    match('-');
                    node = ExpressionNode.sub(node, multitive());
                } catch (final ParseException e2) {
                    setPosition(savePoint2);
                    return node;
                }
            }
        }
    }

    private ExpressionNode multitive() {
        ExpressionNode node = primary();
        for (;;) {
            final int savePoint1 = getPosition();
            try {
                match('*');
                node = ExpressionNode.mul(node, primary());
            } catch (final ParseException e1) {
                setPosition(savePoint1);
                final int savePoint2 = getPosition();
                try {
                    match('/');
                    node = ExpressionNode.div(node, primary());
                } catch (final ParseException e2) {
                    setPosition(savePoint2);
                    return node;
                }
            }
        }
    }

    private ExpressionNode primary() {
        final int savePoint = getPosition();
        try {
            match('(');
            final ExpressionNode node = expression();
            match(')');
            return node;
        } catch (final ParseException e) {
            setPosition(savePoint);
        }
        return integer();
    }

    private ExpressionNode integer() {
        final int savePoint1 = getPosition();
        try {
            return ExpressionNode.val(zero());
        } catch (final ParseException e1) {
            setPosition(savePoint1);

            int value = digitFirst();
            for (;;) {
                final int savePoint2 = getPosition();
                try {
                    value = value * 10 + digitRest();
                } catch (final ParseException e2) {
                    setPosition(savePoint2);
                    return ExpressionNode.val(value);
                }
            }
        }
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
