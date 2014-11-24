package sample;

import org.junit.Test;

import sample.event.impl.Button;
import sample.event.impl.ClickListener;
import sample.observer.impl.HogeObserver;
import sample.observer.impl.HogeSubject;

public class ObserverPatternTest {

    @Test
    public void testObserver() throws Exception {
        HogeSubject subject = new HogeSubject();
        HogeObserver o = new HogeObserver();
        subject.addObserver(o);
        subject.click();
    }

    @Test
    public void testButton() throws Exception {
        Button button = new Button();
        ClickListener listener = new ClickListener();
        button.addListener(listener);
        button.click();
    }
}