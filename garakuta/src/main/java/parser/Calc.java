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

	public static int calc(final String source) {
		final Lexer lexer = new Lexer(source);
		final Parser parser = new Parser(lexer);
		final Ast ast = parser.parse();
		final AstVisitor<Void, Integer> visitor = new AstVisitor<>() {

			@Override
			public Integer visit(final Num ast, final Void param) {
				return ast.value;
			}

			@Override
			public Integer visit(final AddOp ast, final Void param) {
				return ast.left.accept(this, param)
						+ ast.right.accept(this, param);
			}

			@Override
			public Integer visit(final SubOp ast, final Void param) {
				return ast.left.accept(this, param)
						- ast.right.accept(this, param);
			}

			@Override
			public Integer visit(final MulOp ast, final Void param) {
				return ast.left.accept(this, param)
						* ast.right.accept(this, param);
			}

			@Override
			public Integer visit(final DivOp ast, final Void param) {
				return ast.left.accept(this, param)
						/ ast.right.accept(this, param);
			}
		};
		return ast.accept(visitor, null);
	}

	static class Token {
		public final TokenType type;
		public final String text;

		public Token(final TokenType type, final String text) {
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

		public Lexer(final String source) {
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
					final StringBuilder buf = new StringBuilder();
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

		public Num(final Token token) {
			this(token, false);
		}

		public Num(final Token token, final boolean negative) {
			value = Integer.parseInt(token.text) * (negative ? -1 : 1);
		}

		@Override
		public <P, R> R accept(final AstVisitor<P, R> visitor, final P param) {
			return visitor.visit(this, param);
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	static class AddOp extends Ast {
		public final Ast left, right;

		public AddOp(final Ast left, final Ast right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public <P, R> R accept(final AstVisitor<P, R> visitor, final P param) {
			return visitor.visit(this, param);
		}

		@Override
		public String toString() {
			return String.format("(+ %s %s)", left, right);
		}
	}

	static class SubOp extends Ast {
		public final Ast left, right;

		public SubOp(final Ast left, final Ast right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public <P, R> R accept(final AstVisitor<P, R> visitor, final P param) {
			return visitor.visit(this, param);
		}

		@Override
		public String toString() {
			return String.format("(- %s %s)", left, right);
		}
	}

	static class MulOp extends Ast {
		public final Ast left, right;

		public MulOp(final Ast left, final Ast right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public <P, R> R accept(final AstVisitor<P, R> visitor, final P param) {
			return visitor.visit(this, param);
		}

		@Override
		public String toString() {
			return String.format("(* %s %s)", left, right);
		}
	}

	static class DivOp extends Ast {
		public final Ast left, right;

		public DivOp(final Ast left, final Ast right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public <P, R> R accept(final AstVisitor<P, R> visitor, final P param) {
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

		public Parser(final Lexer lexer) {
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

		private void match(final TokenType expected) {
			if (token().type == expected) {
				consume();
			} else {
				throw new ParseException(String.format(
						"expected %s but actual %s", expected, token()));
			}
		}

		public Ast parse() {
			final Ast ast = expr();
			match(TokenType.EOF);
			return ast;
		}

		protected Ast expr() {
			return exprRec(term());
		}

		protected Ast term() {
			return termRec(fact());
		}

		protected Ast add(final Ast left) {
			return binOp(TokenType.ADD, AddOp::new, left, this::term);
		}

		protected Ast sub(final Ast left) {
			return binOp(TokenType.SUB, SubOp::new, left, this::term);
		}

		protected Ast mul(final Ast left) {
			return binOp(TokenType.MUL, MulOp::new, left, this::fact);
		}

		protected Ast div(final Ast left) {
			return binOp(TokenType.DIV, DivOp::new, left, this::fact);
		}

		protected Ast fact() {
			return tryGet(this::paren).orElseGet(this::number);
		}

		protected Ast paren() {
			match(TokenType.L_PAREN);
			final Ast ast = expr();
			match(TokenType.R_PAREN);
			return ast;
		}

		protected Ast number() {
			final Function<Boolean, Ast> f = negative -> {
				final Token token = token();
				match(TokenType.NUMBER);
				return new Num(token, negative);
			};
			final Supplier<Optional<Ast>> tryPlus = () -> tryGet(() -> {
				match(TokenType.ADD);
				return f.apply(false);
			});
			final Supplier<Optional<Ast>> tryMinus = () -> tryGet(() -> {
				match(TokenType.SUB);
				return f.apply(true);
			});
			final Supplier<Ast> num = () -> f.apply(false);
			return Stream.of(tryPlus, tryMinus).map(Supplier::get)
					.filter(Optional::isPresent).map(Optional::get).findFirst()
					.orElseGet(num);
		}

		private Ast binOp(final TokenType expected, final BinaryOperator<Ast> op, final Ast left,
				final Supplier<Ast> right) {
			match(expected);
			return op.apply(left, right.get());
		}

		private Ast exprRec(final Ast left) {
			return tryRec(left, this::exprRec, this::add, this::sub);
		}

		private Ast termRec(final Ast left) {
			return tryRec(left, this::termRec, this::mul, this::div);
		}

		@SafeVarargs
		private final Ast tryRec(final Ast left, final UnaryOperator<Ast> op,
				final UnaryOperator<Ast>... fs) {
			final Optional<Ast> opt = Arrays.stream(fs)
					.map(f -> tryGet(() -> f.apply(left)))
					.filter(Optional::isPresent).map(Optional::get).findFirst();
			if (opt.isPresent()) {
				return op.apply(opt.get());
			}
			return left;
		}

		private <T> Optional<T> tryGet(final Supplier<T> supplier) {
			indexes.push(index);
			try {
				final T t = supplier.get();
				indexes.pop();
				return Optional.of(t);
			} catch (final ParseException e) {
				index = indexes.pop();
				return Optional.empty();
			}
		}
	}

	static class ParseException extends RuntimeException {

		public ParseException(final String message) {
			super(message);
		}
	}
}