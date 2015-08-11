package sample.getlaststatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Visitorパターンバージョン
 *
 */
public class GetLastStatusSample2 {

    public static void main(String[] args) {

        List<Event> events = new ArrayList<>();
        events.add(createStatusEvent(LocalDateTime.of(2015, 8, 11, 17, 27, 00),
                Status.TODO));
        events.add(createOtherEvent(LocalDateTime.of(2015, 8, 11, 17, 28, 00)));
        events.add(createStatusEvent(LocalDateTime.of(2015, 8, 11, 17, 29, 00),
                Status.DOING));
        events.add(createOtherEvent(LocalDateTime.of(2015, 8, 11, 17, 30, 00)));
        events.add(createStatusEvent(LocalDateTime.of(2015, 8, 11, 17, 31, 00),
                Status.DONE));

        Todo todo = new Todo();
        todo.events = events;

        System.out.println(todo.isDone());
    }

    static StatusEvent createStatusEvent(LocalDateTime timestamp, Status status) {
        StatusEvent event = new StatusEvent();
        event.timestamp = timestamp;
        event.status = status;
        return event;
    }

    static OtherEvent createOtherEvent(LocalDateTime timestamp) {
        OtherEvent event = new OtherEvent();
        event.timestamp = timestamp;
        return event;
    }

    static class Todo {
        List<Event> events;

        boolean isDone() {
            Comparator<Event> comparator = Comparator
                    .comparing(event -> event.timestamp);
            EventVisitor<Void, Optional<StatusEvent>> visitor = new EventVisitor<Void, Optional<StatusEvent>>() {

                @Override
                public Optional<StatusEvent> visit(StatusEvent event, Void param) {
                    return Optional.of(event);
                }

                @Override
                public Optional<StatusEvent> visit(OtherEvent event, Void param) {
                    return Optional.empty();
                }
            };
            return events.stream().sorted(comparator.reversed())
                    .map(event -> event.accept(visitor, null))
                    .filter(Optional::isPresent).map(Optional::get).findFirst()
                    .map(event -> event.status == Status.DONE).orElse(false);
        }
    }

    static abstract class Event {
        LocalDateTime timestamp;

        abstract <P, R> R accept(EventVisitor<P, R> visitor, P param);
    }

    static class StatusEvent extends Event {
        Status status;

        @Override
        <P, R> R accept(EventVisitor<P, R> visitor, P param) {
            return visitor.visit(this, param);
        }
    }

    static class OtherEvent extends Event {
        @Override
        <P, R> R accept(EventVisitor<P, R> visitor, P param) {
            return visitor.visit(this, param);
        }
    }

    enum Status {
        TODO, DOING, DONE
    }

    interface EventVisitor<P, R> {
        R visit(StatusEvent event, P param);

        R visit(OtherEvent event, P param);
    }
}
