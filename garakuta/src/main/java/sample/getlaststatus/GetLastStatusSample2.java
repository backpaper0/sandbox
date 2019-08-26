package sample.getlaststatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Visitorパターンバージョン
 *
 * StatusEvent.acceptを実行
 * →EventVisitor.visit(StatusEvent, P)を実行
 * →Optional.of(StatusEvent)を返す
 * 
 * といった流れ。
 * 
 * Visitorパターンはデータと処理を分離するパターン。
 * そして今回はEventに対する細かい処理が多くありそうなので
 * Visitorパターンを採用しても良いかもL(’ω’)┘三└(’ω’)」ｼｭｯｼｭｯ
 * 
 * ただ、VisitorパターンはIteratorパターンやObserverパターンよりも
 * 理解しにくいと感じる(・_・)
 */
public class GetLastStatusSample2 {

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
            final EventVisitor<Void, Optional<StatusEvent>> visitor = new EventVisitor<>() {

                @Override
                public Optional<StatusEvent> visit(final StatusEvent event, final Void param) {
                    return Optional.of(event);
                }

                @Override
                public Optional<StatusEvent> visit(final OtherEvent event, final Void param) {
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
        <P, R> R accept(final EventVisitor<P, R> visitor, final P param) {
            return visitor.visit(this, param);
        }
    }

    static class OtherEvent extends Event {
        @Override
        <P, R> R accept(final EventVisitor<P, R> visitor, final P param) {
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

    //↓こんな感じのスケルトンがあるとvisitメソッドが多いときに便利〜(今回は使ってない)
    static abstract class DefaultEventVisitor<P, R> implements
            EventVisitor<P, R> {

        protected R defaultAction(final Event event, final P param) {
            throw new UnsupportedOperationException();
        }

        @Override
        public R visit(final StatusEvent event, final P param) {
            return defaultAction(event, param);
        }

        @Override
        public R visit(final OtherEvent event, final P param) {
            return defaultAction(event, param);
        }
    }

}
