package com.example;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ServerExtension implements BeforeEachCallback, AfterEachCallback {

    private App.Server server;

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        server = new App().start();
    }

    @Override
    public void afterEach(final ExtensionContext context) throws Exception {
        server.stop();
    }
}
