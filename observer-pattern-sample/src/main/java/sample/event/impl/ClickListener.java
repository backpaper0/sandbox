package sample.event.impl;

import sample.event.EventListener;

public class ClickListener implements EventListener {

    @Override
    public void onAction() {
        System.out.println("ボタンが押されました！");
    }

}
