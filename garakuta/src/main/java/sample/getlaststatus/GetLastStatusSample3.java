package sample.getlaststatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * instanceofを使わずVisitorよりはシンプルなバージョン
 *
 */
public class GetLastStatusSample3 {

	public static void main(final String[] args) {

		final List<Event> events = new ArrayList<>();
		events.add(createStatusEvent(LocalDateTime.of(2015, 8, 11, 17, 27, 00),
				Status.TODO));
		events.add(createOtherEvent(LocalDateTime.of(2015, 8, 11, 17, 28, 00)));
		events.add(createStatusEvent(LocalDateTime.of(2015, 8, 11, 17, 29, 00),
				Status.DOING));
		events.add(createOtherEvent(LocalDateTime.of(2015, 8, 11, 17, 30, 00)));
		events.add(createStatusEvent(LocalDateTime.of(2015, 8, 11, 17, 31, 00),
				Status.DONE));

		final Todo todo = new Todo();
		todo.events = events;

		System.out.println(todo.isDone());
	}

	static StatusEvent createStatusEvent(final LocalDateTime timestamp, final Status status) {
		final StatusEvent event = new StatusEvent();
		event.timestamp = timestamp;
		event.status = status;
		return event;
	}

	static OtherEvent createOtherEvent(final LocalDateTime timestamp) {
		final OtherEvent event = new OtherEvent();
		event.timestamp = timestamp;
		return event;
	}

	static class Todo {
		List<Event> events;

		boolean isDone() {
			final Comparator<Event> comparator = Comparator
					.comparing(event -> event.timestamp);
			return events.stream().sorted(comparator.reversed())
					.map(event -> event.tryToStatusEvent())
					.filter(Optional::isPresent).map(Optional::get).findFirst()
					.map(event -> event.status == Status.DONE).orElse(false);
		}
	}

	static abstract class Event {
		LocalDateTime timestamp;

		abstract Optional<StatusEvent> tryToStatusEvent();
	}

	static class StatusEvent extends Event {
		Status status;

		@Override
		Optional<StatusEvent> tryToStatusEvent() {
			return Optional.of(this);
		}
	}

	static class OtherEvent extends Event {

		@Override
		Optional<StatusEvent> tryToStatusEvent() {
			return Optional.empty();
		}
	}

	enum Status {
		TODO, DOING, DONE
	}
}
