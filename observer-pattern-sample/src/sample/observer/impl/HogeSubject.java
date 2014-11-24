package sample.observer.impl;

import sample.observer.Subject;

public class HogeSubject extends Subject {

    public void click() {
        notifyObservers();
    }

}
