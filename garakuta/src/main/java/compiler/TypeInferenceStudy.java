package compiler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import compiler.TypeInferenceStudy.Ast.BinOp;
import compiler.TypeInferenceStudy.Ast.BinOpType;
import compiler.TypeInferenceStudy.Ast.Bool;
import compiler.TypeInferenceStudy.Ast.Expr;
import compiler.TypeInferenceStudy.Ast.Id;
import compiler.TypeInferenceStudy.Ast.IfThenElse;
import compiler.TypeInferenceStudy.Ast.Int;
import compiler.TypeInferenceStudy.Ast.Let;
import compiler.TypeInferenceStudy.Ast.Let2;

/**
 * @see http://www.fos.kuis.kyoto-u.ac.jp/~igarashi/class/isle4-06w/text/miniml006.html
 * @see http://www.fos.kuis.kyoto-u.ac.jp/~igarashi/class/isle4-06w/text/miniml007.html
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
        eval("let x = 123;;");
        eval("let a = 10 in a + 1;;");
    }

    private static void eval(String source) {
        System.out.println(source);
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Ast ast = parser.parse();
        Evaluator evaluator = new Evaluator();
        try {
            System.out.println(ast);
            ast.accept(evaluator);
            System.out.println(evaluator.getResultValue());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
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

        @Override
        public void visit(Let expr) {
            expr.expr1.accept(this);
            env.put(expr.id.name, stack.pop());
            expr.expr2.accept(this);
        }

        @Override
        public void visit(Let2 expr) {
            expr.expr.accept(this);
            env.put(expr.id.name, stack.pop());
        }
    }

    enum TokenType {
        EOF, IF, THEN, ELSE, BOOL, L_PAREN, R_PAREN, ADD_OP, MUL_OP, LT_OP, INT, ID, SEMICOLON,
        AND_OP, OR_OP, LET, EQ_OP, IN;
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
            case '=':
                consume();
                return new Token(TokenType.EQ_OP, "=");
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
                    case "let":
                        return new Token(TokenType.LET, text);
                    case "in":
                        return new Token(TokenType.IN, text);
                    }
                    return new Token(TokenType.ID, text);
                }
                throw new RuntimeException();
            }
        }
    }

    interface Ast {
        interface Expr extends Ast {
        }

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

        static class Let implements Expr {
            Id id;
            Expr expr1;
            Expr expr2;
            public Let(Id id, Expr expr1, Expr expr2) {
                this.id = id;
                this.expr1 = expr1;
                this.expr2 = expr2;
            }
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }
            @Override
            public String toString() {
                return String.format("LET(%s, %s, %s)", id, expr1, expr2);
            }
        }

        static class Let2 implements Ast {
            Id id;
            Expr expr;
            public Let2(Id id, Expr expr) {
                this.id = id;
                this.expr = expr;
            }
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }
            @Override
            public String toString() {
                return String.format("LET(%s, %s)", id, expr);
            }
        }

        void accept(Visitor visitor);

        interface Visitor {
            void visit(Id expr);
            void visit(Let expr);
            void visit(Let2 expr);
            void visit(Int expr);
            void visit(Bool expr);
            void visit(BinOp expr);
            void visit(IfThenElse expr);
        }
    }

    static class Parser {
        Lexer lexer;
        Token token;
        Queue<Token> tokens = new LinkedList<>();
        List<Token> rollback = new ArrayList<>();
        public Parser(Lexer lexer) {
            this.lexer = lexer;
            consume();
        }
        private void consume() {
            token = tokens.poll();
            if (token == null) {
                token = lexer.nextToken();
            }
            rollback.add(token);
        }
        void rollback() {
            tokens.addAll(rollback);
            rollback.clear();
            consume();
        }
        void commit() {
            rollback.clear();
        }
        Ast parse() {
            try {
                try {
                    Expr expr = expr();
                    match(TokenType.SEMICOLON);
                    match(TokenType.SEMICOLON);
                    match(TokenType.EOF);
                    commit();
                    return expr;
                } catch (ParseException e) {
                    rollback();
                }
                Let2 let = let2();
                match(TokenType.SEMICOLON);
                match(TokenType.SEMICOLON);
                match(TokenType.EOF);
                commit();
                return let;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        private void match(TokenType expected) throws ParseException {
            if (token.tokenType == expected) {
                consume();
            } else {
                throw new ParseException("", 0);
            }
        }
        Expr expr() throws ParseException {
            return or();
        }
        Expr or() throws ParseException {
            Expr left = and();
            while (token.tokenType == TokenType.OR_OP) {
                consume();
                Expr right = and();
                left = new BinOp(BinOpType.OR, left, right);
            }
            return left;
        }
        Expr and() throws ParseException {
            Expr left = lessThan();
            while (token.tokenType == TokenType.AND_OP) {
                consume();
                Expr right = lessThan();
                left = new BinOp(BinOpType.AND, left, right);
            }
            return left;
        }
        Expr lessThan() throws ParseException {
            Expr left = add();
            while (token.tokenType == TokenType.LT_OP) {
                consume();
                Expr right = add();
                left = new BinOp(BinOpType.LT, left, right);
            }
            return left;
        }
        Expr add() throws ParseException {
            Expr left = mul();
            while (token.tokenType == TokenType.ADD_OP) {
                consume();
                Expr right = mul();
                left = new BinOp(BinOpType.ADD, left, right);
            }
            return left;
        }
        Expr mul() throws ParseException {
            Expr left = other();
            while (token.tokenType == TokenType.MUL_OP) {
                consume();
                Expr right = other();
                left = new BinOp(BinOpType.MUL, left, right);
            }
            return left;
        }
        Expr other() throws ParseException {
            switch (token.tokenType) {
            case BOOL: {
                boolean value = Boolean.parseBoolean(token.text);
                consume();
                return new Bool(value);
            }
            case ID: {
                return id();
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
            case LET: {
                consume();
                Id id = id();
                match(TokenType.EQ_OP);
                Expr expr1 = expr();
                match(TokenType.IN);
                Expr expr2 = expr();
                return new Let(id, expr1, expr2);
            }
            default:
                throw new ParseException("", 0);
            }
        }

        Id id() throws ParseException {
            String name = token.text;
            match(TokenType.ID);
            return new Id(name);
        }

        Let2 let2() throws ParseException {
            match(TokenType.LET);
            Id id = id();
            match(TokenType.EQ_OP);
            Expr expr = expr();
            return new Let2(id, expr);
        }
    }
}
