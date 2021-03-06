import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

// 5リットル入る容器が1個、3リットル入る容器が1個ある。
// 水はいくらでも使えるものとして、二つの容器を使って、
// 正確に4リットルの水を量るにはどうすれば良いか。

public class Buckets {

	public static void main(final String[] args) {
		final State state = State.empty();
		final History history = History.empty();
		final Optional<History> opt = actAll(state, history);
		System.out.println(opt.map(Objects::toString).orElse("x"));
	}

	static Optional<History> actAll(final State state, final History history) {
		final List<Action> actions = new ArrayList<>();
		actions.add(new Action("Fill A", State::fill5));
		actions.add(new Action("Fill B", State::fill3));
		actions.add(new Action("Dump A", State::dump5));
		actions.add(new Action("Dump B", State::dump3));
		actions.add(new Action("Pour from A to B", State::pour5to3));
		actions.add(new Action("Pour from B to A", State::pour3to5));
		for (final Action action : actions) {
			final Optional<History> opt = act(state, history, action);
			if (opt.isPresent()) {
				return opt;
			}
		}
		return Optional.empty();
	}

	static Optional<History> act(final State state, final History history, final Action action) {
		final State newState = action.act(state);
		final History newHistory = history.add(action, newState);
		if (newState.five == 4) {
			return Optional.of(newHistory);
		}
		if (history.contains(newState)) {
			return Optional.empty();
		}
		return actAll(newState, newHistory);
	}

	static class Action {
		final String name;
		final UnaryOperator<State> operator;

		public Action(final String name, final UnaryOperator<State> operator) {
			this.name = name;
			this.operator = operator;
		}

		State act(final State state) {
			return operator.apply(state);
		}

		@Override
		public String toString() {
			return name;
		}
	}

	static class Step {
		final Action action;
		final State state;

		public Step(final Action action, final State state) {
			this.action = action;
			this.state = state;
		}

		public boolean contains(final State state) {
			return this.state.equals(state);
		}

		@Override
		public String toString() {
			return String.format("%s: %s", state, action);
		}
	}

	static class History {
		final List<Step> value;

		public History(final List<Step> value) {
			this.value = Collections.unmodifiableList(value);
		}

		static History empty() {
			return new History(Collections.emptyList());
		}

		History add(final Action action, final State state) {
			final List<Step> newValue = new ArrayList<>(value);
			newValue.add(new Step(action, state));
			return new History(newValue);
		}

		boolean contains(final State state) {
			return value.stream().anyMatch(step -> step.contains(state));
		}

		@Override
		public String toString() {
			return value.stream().map(Objects::toString)
					.collect(Collectors.joining(System.lineSeparator()));
		}
	}

	static class State {
		final int five;
		final int three;

		public State(final int five, final int three) {
			this.five = five;
			this.three = three;
		}

		static State empty() {
			return new State(0, 0);
		}

		State fill5() {
			return new State(5, three);
		}

		State fill3() {
			return new State(five, 3);
		}

		State dump5() {
			return new State(0, three);
		}

		State dump3() {
			return new State(five, 0);
		}

		State pour5to3() {
			final int x = Math.min(3 - three, five);
			return new State(five - x, three + x);
		}

		State pour3to5() {
			final int x = Math.min(5 - five, three);
			return new State(five + x, three - x);
		}

		@Override
		public int hashCode() {
			return Objects.hash(five, three);
		}

		@Override
		public boolean equals(final Object obj) {
			return obj instanceof State
					&& five == ((State) obj).five
					&& three == ((State) obj).three;
		}

		@Override
		public String toString() {
			return String.format("A(%d/5), B(%d/3)", five, three);
		}
	}
}
