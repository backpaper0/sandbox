package com.example;

public class Base3Listener {

    public void handlePrePersist(Base3 base) {
        base.setVersion(0);
    }

    public void handlePreUpdate(Base3 base) {
        base.setVersion(base.getVersion() + 1);
    }
}
