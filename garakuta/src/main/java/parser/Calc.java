package parser;

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
        private Token token;

        public Parser(Lexer lexer) {
            this.lexer = lexer;
            consume();
        }

        private void consume() {
            token = lexer.next();
        }

        private void match(TokenType expected) {
            if (token.type == expected) {
                consume();
            } else {
                throw new RuntimeException(String.format(
                        "expected %s but actual %s", expected, token));
            }
        }

        public Ast parse() {
            Ast ast = expr();
            match(TokenType.EOF);
            return ast;
        }

        private Ast expr() {
            Ast ast = term();
            while (token.type == TokenType.ADD || token.type == TokenType.SUB) {
                if (token.type == TokenType.ADD) {
                    match(TokenType.ADD);
                    ast = new AddOp(ast, term());
                } else {
                    match(TokenType.SUB);
                    ast = new SubOp(ast, term());
                }
            }
            return ast;
        }

        private Ast term() {
            Ast ast = fact();
            while (token.type == TokenType.MUL || token.type == TokenType.DIV) {
                if (token.type == TokenType.MUL) {
                    match(TokenType.MUL);
                    ast = new MulOp(ast, fact());
                } else {
                    match(TokenType.DIV);
                    ast = new DivOp(ast, fact());
                }
            }
            return ast;
        }

        private Ast fact() {
            if (token.type != TokenType.L_PAREN) {
                return number();
            }
            match(TokenType.L_PAREN);
            Ast ast = expr();
            match(TokenType.R_PAREN);
            return ast;
        }

        private Ast number() {
            Token token = this.token;
            match(TokenType.NUMBER);
            return new Num(token);
        }
    }
}