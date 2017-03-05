package compiler;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import compiler.TypeInferenceStudy.Expr.BinOp;
import compiler.TypeInferenceStudy.Expr.BinOpType;
import compiler.TypeInferenceStudy.Expr.Bool;
import compiler.TypeInferenceStudy.Expr.Id;
import compiler.TypeInferenceStudy.Expr.IfThenElse;
import compiler.TypeInferenceStudy.Expr.Int;

/**
 * @see http://www.fos.kuis.kyoto-u.ac.jp/~igarashi/class/isle4-06w/text/miniml006.html
 */
public class TypeInferenceStudy {

    public static void main(String[] args) {
        eval("3;;");
        eval("true;;");
        eval("x;;");
        eval("3 + x';;");
        eval("(3 + x1) * false;;");
        eval("1 + 2 * 3 + 4;;");
        eval("true || false;;");
        eval("1 < 2 && true && 3 < x1;;");
    }

    private static void eval(String source) {
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parse();
        Evaluator evaluator = new Evaluator();
        try {
            System.out.println(expr);
            expr.accept(evaluator);
            System.out.println(evaluator.getResultValue());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static class Evaluator implements Expr.Visitor {

        Deque<Object> stack = new LinkedList<>();
        Map<String, Object> env = new HashMap<>();

        public Evaluator() {
            env.put("x", 123);
            env.put("x'", 2);
            env.put("x1", 5);
        }

        Object getResultValue() {
            return stack.pop();
        }

        @Override
        public void visit(Id expr) {
            Object value = env.get(expr.name);
            stack.push(value);
        }

        @Override
        public void visit(Int expr) {
            stack.push(expr.value);
        }

        @Override
        public void visit(Bool expr) {
            stack.push(expr.value);
        }

        @Override
        public void visit(BinOp expr) {
            expr.left.accept(this);
            Object left = stack.pop();
            expr.right.accept(this);
            Object right = stack.pop();
            switch (expr.binOpType) {
            case ADD:
                stack.push(((int) left) + ((int) right));
                break;
            case MUL:
                stack.push(((int) left) * ((int) right));
                break;
            case LT:
                stack.push(((int) left) < ((int) right));
                break;
            case AND:
                stack.push(((boolean) left) && ((boolean) right));
                break;
            case OR:
                stack.push(((boolean) left) || ((boolean) right));
                break;
            }
        }

        @Override
        public void visit(IfThenElse expr) {
            expr.cond.accept(this);
            boolean cond = (boolean) stack.pop();
            if (cond) {
                expr.then.accept(this);
            } else {
                expr.else_.accept(this);
            }
        }
    }

    enum TokenType {
        EOF, IF, THEN, ELSE, BOOL, L_PAREN, R_PAREN, ADD_OP, MUL_OP, LT_OP, INT, ID, SEMICOLON,
        AND_OP, OR_OP;
    }

    static class Token {
        TokenType tokenType;
        String text;
        public Token(TokenType tokenType, String text) {
            this.tokenType = tokenType;
            this.text = text;
        }
        @Override
        public String toString() {
            return String.format("%s(%s)", tokenType, text);
        }
    }

    static class Lexer {

        static char EOF = (char) -1;
        private String source;
        int index;
        char c;

        public Lexer(String source) {
            this.source = source;
            index = 0;
            consume();
        }

        private void consume() {
            if (index < source.length()) {
                c = source.charAt(index++);
            } else {
                c = EOF;
            }
        }

        Token nextToken() {
            while (Character.isWhitespace(c)) {
                consume();
            }
            if (c == EOF) {
                return new Token(TokenType.EOF, "<EOF>");
            }
            switch (c) {
            case ';':
                consume();
                return new Token(TokenType.SEMICOLON, ";");
            case '(':
                consume();
                return new Token(TokenType.L_PAREN, "(");
            case ')':
                consume();
                return new Token(TokenType.R_PAREN, ")");
            case '+':
                consume();
                return new Token(TokenType.ADD_OP, "+");
            case '*':
                consume();
                return new Token(TokenType.MUL_OP, "*");
            case '<':
                consume();
                return new Token(TokenType.LT_OP, "<");
            case '&':
                consume();
                if (c == '&') {
                    consume();
                    return new Token(TokenType.AND_OP, "&&");
                }
                throw new RuntimeException();
            case '|':
                consume();
                if (c == '|') {
                    consume();
                    return new Token(TokenType.OR_OP, "||");
                }
                throw new RuntimeException();
            default:
                if ('0' <= c && c <= '9') {
                    StringBuilder buf = new StringBuilder();
                    do {
                        buf.append(c);
                        consume();
                    } while ('0' <= c && c <= '9');
                    return new Token(TokenType.INT, buf.toString());
                }
                if ('a' <= c && c <= 'z') {
                    StringBuilder buf = new StringBuilder();
                    do {
                        buf.append(c);
                        consume();
                    } while (('a' <= c && c <= 'z') || ('0' <= c && c <= '9') || c == '\'');
                    String text = buf.toString();
                    switch (text) {
                    case "if":
                        return new Token(TokenType.IF, "if");
                    case "then":
                        return new Token(TokenType.THEN, "then");
                    case "else":
                        return new Token(TokenType.ELSE, "else");
                    case "true":
                    case "false":
                        return new Token(TokenType.BOOL, text);
                    }
                    return new Token(TokenType.ID, text);
                }
                throw new RuntimeException();
            }
        }
    }

    interface Expr {

        static class Id implements Expr {
            String name;
            public Id(String name) {
                this.name = name;
            }
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }
            @Override
            public String toString() {
                return String.format("ID(%s)", name);
            }
        }

        static class Int implements Expr {
            int value;
            public Int(int value) {
                this.value = value;
            }
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }
            @Override
            public String toString() {
                return String.format("INT(%s)", value);
            }
        }

        static class Bool implements Expr {
            boolean value;
            public Bool(boolean value) {
                this.value = value;
            }
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }
            @Override
            public String toString() {
                return String.format("BOOL(%s)", value);
            }
        }

        enum BinOpType {
            ADD, MUL, LT, AND, OR
        }

        static class BinOp implements Expr {
            BinOpType binOpType;
            Expr left;
            Expr right;
            public BinOp(BinOpType binOpType, Expr left, Expr right) {
                this.binOpType = binOpType;
                this.left = left;
                this.right = right;
            }
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }
            @Override
            public String toString() {
                return String.format("%s(%s, %s)", binOpType, left, right);
            }
        }

        static class IfThenElse implements Expr {
            Expr cond;
            Expr then;
            Expr else_;
            public IfThenElse(Expr cond, Expr then, Expr else_) {
                this.cond = cond;
                this.then = then;
                this.else_ = else_;
            }
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }
            @Override
            public String toString() {
                return String.format("IF(%s, %s, %s)", cond, then, else_);
            }
        }

        void accept(Visitor visitor);

        interface Visitor {
            void visit(Id expr);
            void visit(Int expr);
            void visit(Bool expr);
            void visit(BinOp expr);
            void visit(IfThenElse expr);
        }
    }

    static class Parser {
        Lexer lexer;
        Token token;
        public Parser(Lexer lexer) {
            this.lexer = lexer;
            consume();
        }
        private void consume() {
            token = lexer.nextToken();
        }
        Expr parse() {
            Expr expr = expr();
            match(TokenType.SEMICOLON);
            match(TokenType.SEMICOLON);
            match(TokenType.EOF);
            return expr;
        }
        private void match(TokenType expected) {
            if (token.tokenType == expected) {
                consume();
            } else {
                throw new RuntimeException();
            }
        }
        Expr expr() {
            return or();
        }
        Expr or() {
            Expr left = and();
            while (token.tokenType == TokenType.OR_OP) {
                consume();
                Expr right = and();
                left = new BinOp(BinOpType.OR, left, right);
            }
            return left;
        }
        Expr and() {
            Expr left = lessThan();
            while (token.tokenType == TokenType.AND_OP) {
                consume();
                Expr right = lessThan();
                left = new BinOp(BinOpType.AND, left, right);
            }
            return left;
        }
        Expr lessThan() {
            Expr left = add();
            while (token.tokenType == TokenType.LT_OP) {
                consume();
                Expr right = add();
                left = new BinOp(BinOpType.LT, left, right);
            }
            return left;
        }
        Expr add() {
            Expr left = mul();
            while (token.tokenType == TokenType.ADD_OP) {
                consume();
                Expr right = mul();
                left = new BinOp(BinOpType.ADD, left, right);
            }
            return left;
        }
        Expr mul() {
            Expr left = other();
            while (token.tokenType == TokenType.MUL_OP) {
                consume();
                Expr right = other();
                left = new BinOp(BinOpType.MUL, left, right);
            }
            return left;
        }
        Expr other() {
            switch (token.tokenType) {
            case BOOL: {
                boolean value = Boolean.parseBoolean(token.text);
                consume();
                return new Bool(value);
            }
            case ID: {
                String name = token.text;
                consume();
                return new Id(name);
            }
            case IF: {
                consume();
                Expr cond = expr();
                match(TokenType.THEN);
                Expr then = expr();
                match(TokenType.ELSE);
                Expr else_ = expr();
                return new IfThenElse(cond, then, else_);
            }
            case INT: {
                int value = Integer.parseInt(token.text);
                consume();
                return new Int(value);
            }
            case L_PAREN: {
                consume();
                Expr expr = expr();
                match(TokenType.R_PAREN);
                return expr;
            }
            default:
                throw new RuntimeException();
            }
        }
    }
}
