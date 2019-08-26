package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Calc {

    public static int calc(String source) {
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Ast ast = parser.parse();
        AstVisitor<Void, Integer> visitor = new AstVisitor<>() {

            @Override
            public Integer visit(Num ast, Void param) {
                return ast.value;
            }

            @Override
            public Integer visit(AddOp ast, Void param) {
                return ast.left.accept(this, param)
                        + ast.right.accept(this, param);
            }

            @Override
            public Integer visit(SubOp ast, Void param) {
                return ast.left.accept(this, param)
                        - ast.right.accept(this, param);
            }

            @Override
            public Integer visit(MulOp ast, Void param) {
                return ast.left.accept(this, param)
                        * ast.right.accept(this, param);
            }

            @Override
            public Integer visit(DivOp ast, Void param) {
                return ast.left.accept(this, param)
                        / ast.right.accept(this, param);
            }
        };
        return ast.accept(visitor, null);
    }

    static class Token {
        public final TokenType type;
        public final String text;

        public Token(TokenType type, String text) {
            this.type = type;
            this.text = text;
        }

        @Override
        public String toString() {
            return String.format("%s('%s')", type, text);
        }
    }

    enum TokenType {
        NUMBER, ADD, SUB, MUL, DIV, L_PAREN, R_PAREN, EOF
    }

    static class Lexer {
        private static final char EOF = (char) -1;
        private final String source;
        private int index = -1;
        private char c;

        public Lexer(String source) {
            this.source = source;
            consume();
        }

        private void consume() {
            index++;
            if (index < source.length()) {
                c = source.charAt(index);
            } else {
                c = EOF;
            }
        }

        public Token next() {
            while (c == ' ') {
                consume();
            }
            switch (c) {
            case '+':
                consume();
                return new Token(TokenType.ADD, "+");
            case '-':
                consume();
                return new Token(TokenType.SUB, "-");
            case '*':
                consume();
                return new Token(TokenType.MUL, "*");
            case '/':
                consume();
                return new Token(TokenType.DIV, "/");
            case '(':
                consume();
                return new Token(TokenType.L_PAREN, "(");
            case ')':
                consume();
                return new Token(TokenType.R_PAREN, ")");
            default:
                if ('0' <= c && c <= '9') {
                    StringBuilder buf = new StringBuilder();
                    do {
                        buf.append(c);
                        consume();
                    } while ('0' <= c && c <= '9');
                    return new Token(TokenType.NUMBER, buf.toString());
                }
            }
            return new Token(TokenType.EOF, "<EOF>");
        }
    }

    static abstract class Ast {
        public abstract <P, R> R accept(AstVisitor<P, R> visitor, P param);
    }

    static class Num extends Ast {
        public final int value;

        public Num(Token token) {
            this(token, false);
        }

        public Num(Token token, boolean negative) {
            value = Integer.parseInt(token.text) * (negative ? -1 : 1);
        }

        @Override
        public <P, R> R accept(AstVisitor<P, R> visitor, P param) {
            return visitor.visit(this, param);
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    static class AddOp extends Ast {
        public final Ast left, right;

        public AddOp(Ast left, Ast right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <P, R> R accept(AstVisitor<P, R> visitor, P param) {
            return visitor.visit(this, param);
        }

        @Override
        public String toString() {
            return String.format("(+ %s %s)", left, right);
        }
    }

    static class SubOp extends Ast {
        public final Ast left, right;

        public SubOp(Ast left, Ast right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <P, R> R accept(AstVisitor<P, R> visitor, P param) {
            return visitor.visit(this, param);
        }

        @Override
        public String toString() {
            return String.format("(- %s %s)", left, right);
        }
    }

    static class MulOp extends Ast {
        public final Ast left, right;

        public MulOp(Ast left, Ast right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <P, R> R accept(AstVisitor<P, R> visitor, P param) {
            return visitor.visit(this, param);
        }

        @Override
        public String toString() {
            return String.format("(* %s %s)", left, right);
        }
    }

    static class DivOp extends Ast {
        public final Ast left, right;

        public DivOp(Ast left, Ast right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <P, R> R accept(AstVisitor<P, R> visitor, P param) {
            return visitor.visit(this, param);
        }

        @Override
        public String toString() {
            return String.format("(/ %s %s)", left, right);
        }
    }

    interface AstVisitor<P, R> {
        R visit(Num ast, P param);

        R visit(AddOp ast, P param);

        R visit(SubOp ast, P param);

        R visit(MulOp ast, P param);

        R visit(DivOp ast, P param);
    }

    static class Parser {
        private final Lexer lexer;
        private final List<Token> tokens = new ArrayList<>();
        private int index = -1;
        private final Deque<Integer> indexes = new LinkedList<>();

        public Parser(Lexer lexer) {
            this.lexer = lexer;
            consume();
        }

        private void consume() {
            index++;
            while (tokens.size() <= index) {
                tokens.add(lexer.next());
            }
        }

        private Token token() {
            return tokens.get(index);
        }

        private void match(TokenType expected) {
            if (token().type == expected) {
                consume();
            } else {
                throw new ParseException(String.format(
                        "expected %s but actual %s", expected, token()));
            }
        }

        public Ast parse() {
            Ast ast = expr();
            match(TokenType.EOF);
            return ast;
        }

        protected Ast expr() {
            return exprRec(term());
        }

        protected Ast term() {
            return termRec(fact());
        }

        protected Ast add(Ast left) {
            return binOp(TokenType.ADD, AddOp::new, left, this::term);
        }

        protected Ast sub(Ast left) {
            return binOp(TokenType.SUB, SubOp::new, left, this::term);
        }

        protected Ast mul(Ast left) {
            return binOp(TokenType.MUL, MulOp::new, left, this::fact);
        }

        protected Ast div(Ast left) {
            return binOp(TokenType.DIV, DivOp::new, left, this::fact);
        }

        protected Ast fact() {
            return tryGet(this::paren).orElseGet(this::number);
        }

        protected Ast paren() {
            match(TokenType.L_PAREN);
            Ast ast = expr();
            match(TokenType.R_PAREN);
            return ast;
        }

        protected Ast number() {
            Function<Boolean, Ast> f = negative -> {
                Token token = token();
                match(TokenType.NUMBER);
                return new Num(token, negative);
            };
            Supplier<Optional<Ast>> tryPlus = () -> tryGet(() -> {
                match(TokenType.ADD);
                return f.apply(false);
            });
            Supplier<Optional<Ast>> tryMinus = () -> tryGet(() -> {
                match(TokenType.SUB);
                return f.apply(true);
            });
            Supplier<Ast> num = () -> f.apply(false);
            return Stream.of(tryPlus, tryMinus).map(Supplier::get)
                    .filter(Optional::isPresent).map(Optional::get).findFirst()
                    .orElseGet(num);
        }

        private Ast binOp(TokenType expected, BinaryOperator<Ast> op, Ast left,
                Supplier<Ast> right) {
            match(expected);
            return op.apply(left, right.get());
        }

        private Ast exprRec(Ast left) {
            return tryRec(left, this::exprRec, this::add, this::sub);
        }

        private Ast termRec(Ast left) {
            return tryRec(left, this::termRec, this::mul, this::div);
        }

        @SafeVarargs
        private final Ast tryRec(Ast left, UnaryOperator<Ast> op,
                UnaryOperator<Ast>... fs) {
            Optional<Ast> opt = Arrays.stream(fs)
                    .map(f -> tryGet(() -> f.apply(left)))
                    .filter(Optional::isPresent).map(Optional::get).findFirst();
            if (opt.isPresent()) {
                return op.apply(opt.get());
            }
            return left;
        }

        private <T> Optional<T> tryGet(Supplier<T> supplier) {
            indexes.push(index);
            try {
                T t = supplier.get();
                indexes.pop();
                return Optional.of(t);
            } catch (ParseException e) {
                index = indexes.pop();
                return Optional.empty();
            }
        }
    }

    static class ParseException extends RuntimeException {

        public ParseException(String message) {
            super(message);
        }
    }
}