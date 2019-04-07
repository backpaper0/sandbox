package sample;

import org.junit.jupiter.api.Test;

import sample.event.impl.Button;
import sample.event.impl.ClickListener;
import sample.observer.impl.HogeObserver;
import sample.observer.impl.HogeSubject;

class ObserverPatternTest {

    @Test
    void testObserver() throws Exception {
        final HogeSubject subject = new HogeSubject();
        final HogeObserver o = new HogeObserver();
        subject.addObserver(o);
        subject.click();
    }

    @Test
    void testButton() throws Exception {
        final Button button = new Button();
        final ClickListener listener = new ClickListener();
        button.addListener(listener);
        button.click();
    }
}