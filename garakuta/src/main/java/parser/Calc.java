package parser;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Calc {

    public static int calc(String source) {
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Ast ast = parser.parse();
        AstVisitor<Void, Integer> visitor = new AstVisitor<Void, Integer>() {

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
            value = Integer.parseInt(token.text);
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

        private Ast expr() {
            return exprRec(term());
        }

        private Ast exprRec(Ast left) {
            Optional<Ast> opt = tryAdd(left).map(Optional::of).orElseGet(
                    () -> trySub(left));
            if (opt.isPresent()) {
                return exprRec(opt.get());
            }
            return left;
        }

        private Ast term() {
            return termRec(fact());
        }

        private Ast termRec(Ast left) {
            Optional<Ast> opt = tryMul(left).map(Optional::of).orElseGet(
                    () -> tryDiv(left));
            if (opt.isPresent()) {
                return exprRec(opt.get());
            }
            return left;
        }

        private Ast fact() {
            return tryGet(this::paren).orElseGet(this::number);
        }

        private Ast add(Ast left) {
            match(TokenType.ADD);
            return new AddOp(left, term());
        }

        private Ast sub(Ast left) {
            match(TokenType.SUB);
            return new SubOp(left, term());
        }

        private Ast mul(Ast left) {
            match(TokenType.MUL);
            return new MulOp(left, fact());
        }

        private Ast div(Ast left) {
            match(TokenType.DIV);
            return new DivOp(left, fact());
        }

        private Ast number() {
            Token token = token();
            match(TokenType.NUMBER);
            return new Num(token);
        }

        private Ast paren() {
            match(TokenType.L_PAREN);
            Ast ast = expr();
            match(TokenType.R_PAREN);
            return ast;
        }

        private Optional<Ast> tryAdd(Ast left) {
            return tryGet(() -> add(left));
        }

        private Optional<Ast> trySub(Ast left) {
            return tryGet(() -> sub(left));
        }

        private Optional<Ast> tryMul(Ast left) {
            return tryGet(() -> mul(left));
        }

        private Optional<Ast> tryDiv(Ast left) {
            return tryGet(() -> div(left));
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