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
import compiler.TypeInferenceStudy.Ast.LetDef;
import compiler.TypeInferenceStudy.Ast.LetExpr;

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
        eval("let x = 1\nlet y = x + 1;;");
        eval("let x = 100\nand y = x in x+y;;");
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
            System.out.println(evaluator.getEnvironment());
            System.out.println(evaluator.getResultValue());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    static class Evaluator implements Expr.Visitor {

        Deque<Object> stack = new LinkedList<>();
        Deque<Map<String, Object>> envs = new LinkedList<>();

        public Evaluator() {
            Map<String, Object> env = new HashMap<>();
            env.put("x", 10);
            env.put("x'", 2);
            env.put("x1", 5);
            envs.push(env);
        }

        public Map<String, Object> getEnvironment() {
            return envs.getFirst();
        }

        Object getResultValue() {
            return stack.pop();
        }

        @Override
        public void visit(Id expr) {
            for (Map<String, Object> env : envs) {
                if (env.containsKey(expr.name)) {
                    Object value = env.get(expr.name);
                    stack.push(value);
                    return;
                }
            }
            throw new RuntimeException();
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
        public void visit(LetExpr expr) {
            Map<String, Object> env = new HashMap<>();
            for (LetExpr.One let : expr.list) {
                let.expr.accept(this);
                env.put(let.name, stack.pop());
            }
            envs.push(env);
            expr.expr.accept(this);
            envs.pop();
        }

        @Override
        public void visit(LetDef expr) {
            for (LetDef.One let : expr.list) {
                let.expr.accept(this);
                envs.getFirst().put(let.name, stack.pop());
            }
        }
    }

    enum TokenType {
        EOF, IF, THEN, ELSE, BOOL, L_PAREN, R_PAREN, ADD_OP, MUL_OP, LT_OP, INT, ID, SEMICOLON,
        AND_OP, OR_OP, LET, EQ_OP, IN, AND;
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
                        return new Token(TokenType.IF, text);
                    case "then":
                        return new Token(TokenType.THEN, text);
                    case "else":
                        return new Token(TokenType.ELSE, text);
                    case "true":
                    case "false":
                        return new Token(TokenType.BOOL, text);
                    case "let":
                        return new Token(TokenType.LET, text);
                    case "in":
                        return new Token(TokenType.IN, text);
                    case "and":
                        return new Token(TokenType.AND, text);
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

        static class LetExpr implements Expr {
            List<LetExpr.One> list;
            Expr expr;
            public LetExpr(List<LetExpr.One> list, Expr expr) {
                this.list = list;
                this.expr = expr;
            }
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }
            @Override
            public String toString() {
                return String.format("LET(%s, %s)", list, expr);
            }

            static class One {
                String name;
                Expr expr;
                public One(String name, Expr expr) {
                    this.name = name;
                    this.expr = expr;
                }
                @Override
                public String toString() {
                    return String.format("%s = %s", name, expr);
                }
            }
        }

        static class LetDef implements Ast {
            List<LetDef.One> list;
            public LetDef(List<LetDef.One> list) {
                this.list = list;
            }
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }
            @Override
            public String toString() {
                return list.toString();
            }

            static class One {
                String name;
                Expr expr;
                public One(String name, Expr expr) {
                    this.name = name;
                    this.expr = expr;
                }
                @Override
                public String toString() {
                    return String.format("LET(%s, %s)", name, expr);
                }
            }
        }

        void accept(Visitor visitor);

        interface Visitor {
            void visit(Id expr);
            void visit(LetExpr expr);
            void visit(LetDef expr);
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
                LetDef let = letDef();
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
                return letExpr();
            }
            default:
                throw new ParseException("", 0);
            }
        }

        LetExpr letExpr() throws ParseException {
            List<LetExpr.One> list = new ArrayList<>();
            match(TokenType.LET);
            Id id = id();
            match(TokenType.EQ_OP);
            Expr expr1 = expr();
            list.add(new LetExpr.One(id.name, expr1));
            while (token.tokenType == TokenType.AND) {
                match(TokenType.AND);
                Id id2 = id();
                match(TokenType.EQ_OP);
                Expr expr2 = expr();
                list.add(new LetExpr.One(id2.name, expr2));
            }
            match(TokenType.IN);
            Expr expr3 = expr();
            return new LetExpr(list, expr3);
        }

        Id id() throws ParseException {
            String name = token.text;
            match(TokenType.ID);
            return new Id(name);
        }

        LetDef letDef() throws ParseException {
            List<LetDef.One> list = new ArrayList<>();
            do {
                match(TokenType.LET);
                String name = token.text;
                match(TokenType.ID);
                match(TokenType.EQ_OP);
                Expr expr = expr();
                list.add(new LetDef.One(name, expr));
            } while (token.tokenType == TokenType.LET);
            return new LetDef(list);
        }
    }
}
