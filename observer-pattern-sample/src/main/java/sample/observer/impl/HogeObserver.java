package sample.observer.impl;

import sample.observer.Observer;

public class HogeObserver implements Observer {

    @Override
    public void notifyAction() {
        System.out.println("通知されました！");
    }

}
