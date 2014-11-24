package sample.event;

import java.util.ArrayList;
import java.util.List;

import sample.event.impl.ClickListener;

public abstract class Component {

    private List<EventListener> listeners = new ArrayList<EventListener>();

    public void addListener(ClickListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ClickListener listener) {
        listeners.remove(listener);
    }

    protected void notifyListeners() {
        for (EventListener listener : listeners) {
            listener.onAction();
        }
    }

}
