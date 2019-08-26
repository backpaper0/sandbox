package compiler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import compiler.TypeInferenceStudy.Ast.App;
import compiler.TypeInferenceStudy.Ast.BinOp;
import compiler.TypeInferenceStudy.Ast.BinOpType;
import compiler.TypeInferenceStudy.Ast.Bool;
import compiler.TypeInferenceStudy.Ast.Expr;
import compiler.TypeInferenceStudy.Ast.Fun;
import compiler.TypeInferenceStudy.Ast.Id;
import compiler.TypeInferenceStudy.Ast.IfThenElse;
import compiler.TypeInferenceStudy.Ast.Int;
import compiler.TypeInferenceStudy.Ast.LetDef;
import compiler.TypeInferenceStudy.Ast.LetExpr;

/**
 * @see http://www.fos.kuis.kyoto-u.ac.jp/~igarashi/class/isle4-06w/text/miniml006.html
 * @see http://www.fos.kuis.kyoto-u.ac.jp/~igarashi/class/isle4-06w/text/miniml007.html
 * @see http://www.fos.kuis.kyoto-u.ac.jp/~igarashi/class/isle4-06w/text/miniml008.html
 */
public class TypeInferenceStudy {

    public static void main(final String[] args) {
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
        eval("let x = 2 in\nlet addx = fun y -> x + y in\naddx 4;;");
        eval("((fun x -> fun y -> x + y) 1) 2;;");
        eval("let rec sum = fun x -> if 10 < x then 0 else x + (sum (x + 1)) in sum 0;;");
    }

    private static void eval(final String source) {
        System.out.println(source);
        final Lexer lexer = new Lexer(source);
        final Parser parser = new Parser(lexer);
        final Ast ast = parser.parse();
        final Evaluator evaluator = new Evaluator();
        try {
            System.out.println(ast);
            ast.accept(evaluator);
            System.out.println(evaluator.getEnvironment());
            System.out.println(evaluator.getResultValue());
        } catch (final Exception e) {
            System.out.printf("%s: %s%n", e.getClass(), e.getMessage());
        }
        System.out.println();
    }

    static class Evaluator implements Expr.Visitor {

        Deque<Object> stack = new LinkedList<>();
        Deque<Map<String, Object>> envs = new LinkedList<>();

        public Evaluator() {
            final Map<String, Object> env = new HashMap<>();
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
        public void visit(final Id expr) {
            for (final Map<String, Object> env : envs) {
                if (env.containsKey(expr.name)) {
                    final Object value = env.get(expr.name);
                    stack.push(value);
                    return;
                }
            }
            throw new RuntimeException();
        }

        @Override
        public void visit(final Int expr) {
            stack.push(expr.value);
        }

        @Override
        public void visit(final Bool expr) {
            stack.push(expr.value);
        }

        @Override
        public void visit(final BinOp expr) {
            expr.left.accept(this);
            final Object left = stack.pop();
            expr.right.accept(this);
            final Object right = stack.pop();
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
        public void visit(final IfThenElse expr) {
            expr.cond.accept(this);
            final boolean cond = (boolean) stack.pop();
            if (cond) {
                expr.then.accept(this);
            } else {
                expr.else_.accept(this);
            }
        }

        @Override
        public void visit(final LetExpr expr) {
            final Map<String, Object> env = new HashMap<>();
            for (final LetExpr.One let : expr.list) {
                let.expr.accept(this);
                final Object a = stack.pop();
                if (let.rec && a instanceof Invoker) {
                    ((Invoker) a).envs.add(env);
                }
                env.put(let.name, a);
            }
            envs.push(env);
            expr.expr.accept(this);
            envs.pop();
        }

        @Override
        public void visit(final LetDef expr) {
            for (final LetDef.One let : expr.list) {
                let.expr.accept(this);
                envs.getFirst().put(let.name, stack.pop());
            }
        }

        @Override
        public void visit(final Fun fun) {
            final Invoker inv = new Invoker();
            inv.fun = fun;
            inv.envs.addAll(envs);
            stack.push(inv);
        }

        @Override
        public void visit(final App app) {
            app.expr1.accept(this);
            final Invoker inv = (Invoker) stack.pop();
            app.expr2.accept(this);
            final Deque<Map<String, Object>> original = envs;
            envs = inv.envs;
            final Map<String, Object> env = new HashMap<>();
            final Object parameter = stack.pop();
            env.put(inv.fun.id.name, parameter);
            envs.push(env);
            inv.fun.expr.accept(this);
            envs.pop();
            envs = original;
        }

        static class Invoker {
            Deque<Map<String, Object>> envs = new LinkedList<>();
            Fun fun;
        }
    }

    enum TokenType {
        EOF, IF, THEN, ELSE, BOOL, L_PAREN, R_PAREN, ADD_OP, MUL_OP, LT_OP, INT, ID, SEMICOLON,
        AND_OP, OR_OP, LET, EQ_OP, IN, AND, FUN, ARROW, REC;
    }

    static class Token {
        TokenType tokenType;
        String text;

        public Token(final TokenType tokenType, final String text) {
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
        private final String source;
        int index;
        char c;

        public Lexer(final String source) {
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
            case '-':
                consume();
                if (c == '>') {
                    consume();
                    return new Token(TokenType.ARROW, "->");
                }
                throw new RuntimeException();
            default:
                if ('0' <= c && c <= '9') {
                    final StringBuilder buf = new StringBuilder();
                    do {
                        buf.append(c);
                        consume();
                    } while ('0' <= c && c <= '9');
                    return new Token(TokenType.INT, buf.toString());
                }
                if ('a' <= c && c <= 'z') {
                    final StringBuilder buf = new StringBuilder();
                    do {
                        buf.append(c);
                        consume();
                    } while (('a' <= c && c <= 'z') || ('0' <= c && c <= '9') || c == '\'');
                    final String text = buf.toString();
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
                    case "fun":
                        return new Token(TokenType.FUN, text);
                    case "rec":
                        return new Token(TokenType.REC, text);
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

            public Id(final String name) {
                this.name = name;
            }

            @Override
            public void accept(final Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public String toString() {
                return String.format("ID(%s)", name);
            }
        }

        static class Int implements Expr {
            int value;

            public Int(final int value) {
                this.value = value;
            }

            @Override
            public void accept(final Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public String toString() {
                return String.format("INT(%s)", value);
            }
        }

        static class Bool implements Expr {
            boolean value;

            public Bool(final boolean value) {
                this.value = value;
            }

            @Override
            public void accept(final Visitor visitor) {
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

            public BinOp(final BinOpType binOpType, final Expr left, final Expr right) {
                this.binOpType = binOpType;
                this.left = left;
                this.right = right;
            }

            @Override
            public void accept(final Visitor visitor) {
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

            public IfThenElse(final Expr cond, final Expr then, final Expr else_) {
                this.cond = cond;
                this.then = then;
                this.else_ = else_;
            }

            @Override
            public void accept(final Visitor visitor) {
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

            public LetExpr(final List<LetExpr.One> list, final Expr expr) {
                this.list = list;
                this.expr = expr;
            }

            @Override
            public void accept(final Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public String toString() {
                return String.format("LET(%s, %s)", list, expr);
            }

            static class One {
                String name;
                Expr expr;
                boolean rec;

                public One(final boolean rec, final String name, final Expr expr) {
                    this.rec = rec;
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

            public LetDef(final List<LetDef.One> list) {
                this.list = list;
            }

            @Override
            public void accept(final Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public String toString() {
                return list.toString();
            }

            static class One {
                String name;
                Expr expr;
                boolean rec;

                public One(final boolean rec, final String name, final Expr expr) {
                    this.rec = rec;
                    this.name = name;
                    this.expr = expr;
                }

                @Override
                public String toString() {
                    return String.format("LET(%s, %s)", name, expr);
                }
            }
        }

        static class Fun implements Expr {
            Id id;
            Expr expr;

            public Fun(final Id id, final Expr expr) {
                this.id = id;
                this.expr = expr;
            }

            @Override
            public void accept(final Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public String toString() {
                return String.format("FUN(%s -> %s)", id, expr);
            }
        }

        static class App implements Expr {
            Expr expr1;
            Expr expr2;

            public App(final Expr expr1, final Expr expr2) {
                this.expr1 = expr1;
                this.expr2 = expr2;
            }

            @Override
            public void accept(final Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public String toString() {
                return String.format("APP(%s %s)", expr1, expr2);
            }
        }

        void accept(Visitor visitor);

        interface Visitor {
            void visit(Id id);

            void visit(App app);

            void visit(Fun fun);

            void visit(LetExpr letExpr);

            void visit(LetDef letDef);

            void visit(Int int_);

            void visit(Bool bool);

            void visit(BinOp binOp);

            void visit(IfThenElse ifThenElse);
        }
    }

    static class Parser {
        Lexer lexer;
        Token token;
        List<Token> tokens = new ArrayList<>();
        int index;

        public Parser(final Lexer lexer) {
            this.lexer = lexer;
            consume();
        }

        private void consume() {
            if (index < tokens.size()) {
                token = tokens.get(index);
            } else {
                tokens.add(token = lexer.nextToken());
            }
            index++;
        }

        void rollback(final int index) {
            this.index = index - 1;
            consume();
        }

        Ast parse() {
            try {
                final int index = this.index;
                try {
                    final Expr expr = expr();
                    match(TokenType.SEMICOLON);
                    match(TokenType.SEMICOLON);
                    match(TokenType.EOF);
                    return expr;
                } catch (final ParseException e) {
                    rollback(index);
                }
                final LetDef let = letDef();
                match(TokenType.SEMICOLON);
                match(TokenType.SEMICOLON);
                match(TokenType.EOF);
                return let;
            } catch (final ParseException e) {
                throw new RuntimeException(e);
            }
        }

        private void match(final TokenType expected) throws ParseException {
            if (token.tokenType == expected) {
                consume();
            } else {
                throw new ParseException("", 0);
            }
        }

        Expr expr() throws ParseException {
            final int index = this.index;
            try {
                return app();
            } catch (final ParseException e) {
                rollback(index);
            }
            return or();
        }

        App app() throws ParseException {
            final Expr expr1 = or();
            final Expr expr2 = or();
            return new App(expr1, expr2);
        }

        Expr or() throws ParseException {
            Expr left = and();
            while (token.tokenType == TokenType.OR_OP) {
                consume();
                final Expr right = and();
                left = new BinOp(BinOpType.OR, left, right);
            }
            return left;
        }

        Expr and() throws ParseException {
            Expr left = lessThan();
            while (token.tokenType == TokenType.AND_OP) {
                consume();
                final Expr right = lessThan();
                left = new BinOp(BinOpType.AND, left, right);
            }
            return left;
        }

        Expr lessThan() throws ParseException {
            Expr left = add();
            while (token.tokenType == TokenType.LT_OP) {
                consume();
                final Expr right = add();
                left = new BinOp(BinOpType.LT, left, right);
            }
            return left;
        }

        Expr add() throws ParseException {
            Expr left = mul();
            while (token.tokenType == TokenType.ADD_OP) {
                consume();
                final Expr right = mul();
                left = new BinOp(BinOpType.ADD, left, right);
            }
            return left;
        }

        Expr mul() throws ParseException {
            Expr left = other();
            while (token.tokenType == TokenType.MUL_OP) {
                consume();
                final Expr right = other();
                left = new BinOp(BinOpType.MUL, left, right);
            }
            return left;
        }

        Expr other() throws ParseException {
            switch (token.tokenType) {
            case BOOL: {
                final boolean value = Boolean.parseBoolean(token.text);
                consume();
                return new Bool(value);
            }
            case ID: {
                return id();
            }
            case IF: {
                consume();
                final Expr cond = expr();
                match(TokenType.THEN);
                final Expr then = expr();
                match(TokenType.ELSE);
                final Expr else_ = expr();
                return new IfThenElse(cond, then, else_);
            }
            case INT: {
                final int value = Integer.parseInt(token.text);
                consume();
                return new Int(value);
            }
            case L_PAREN: {
                consume();
                final Expr expr = expr();
                match(TokenType.R_PAREN);
                return expr;
            }
            case LET: {
                return letExpr();
            }
            case FUN: {
                return fun();
            }
            default:
                throw new ParseException("", 0);
            }
        }

        private Fun fun() throws ParseException {
            match(TokenType.FUN);
            final Id id = id();
            match(TokenType.ARROW);
            final Expr expr = expr();
            return new Fun(id, expr);
        }

        LetExpr letExpr() throws ParseException {
            final List<LetExpr.One> list = new ArrayList<>();
            match(TokenType.LET);
            boolean rec;
            if (rec = (token.tokenType == TokenType.REC)) {
                consume();
            }
            final Id id = id();
            match(TokenType.EQ_OP);
            final Expr expr1 = expr();
            list.add(new LetExpr.One(rec, id.name, expr1));
            while (token.tokenType == TokenType.AND) {
                match(TokenType.AND);
                boolean rec2;
                if (rec2 = (token.tokenType == TokenType.REC)) {
                    consume();
                }
                final Id id2 = id();
                match(TokenType.EQ_OP);
                final Expr expr2 = expr();
                list.add(new LetExpr.One(rec2, id2.name, expr2));
            }
            match(TokenType.IN);
            final Expr expr3 = expr();
            return new LetExpr(list, expr3);
        }

        Id id() throws ParseException {
            final String name = token.text;
            match(TokenType.ID);
            return new Id(name);
        }

        LetDef letDef() throws ParseException {
            final List<LetDef.One> list = new ArrayList<>();
            do {
                match(TokenType.LET);
                boolean rec;
                if (rec = (token.tokenType == TokenType.REC)) {
                    consume();
                }
                final String name = token.text;
                match(TokenType.ID);
                match(TokenType.EQ_OP);
                final Expr expr = expr();
                list.add(new LetDef.One(rec, name, expr));
            } while (token.tokenType == TokenType.LET);
            return new LetDef(list);
        }
    }
}
