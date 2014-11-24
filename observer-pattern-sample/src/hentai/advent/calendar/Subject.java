package hentai.advent.calendar;

import java.util.concurrent.CopyOnWriteArrayList;

public class Subject {

    private CopyOnWriteArrayList<Observer> observers = new CopyOnWriteArrayList<Observer>();

    protected void notifyObservers(Event event) {
        for (Observer o : observers) {
            o.notify(event);
        }
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void deleteObserver(Observer o) {
        observers.remove(o);
    }

}
