package sample.event.impl;

import sample.event.Component;

public class Button extends Component {

    public void click() {
        notifyListeners();
    }
}
