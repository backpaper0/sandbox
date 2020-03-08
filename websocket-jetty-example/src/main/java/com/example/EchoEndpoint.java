package com.example;

import java.io.IOException;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/echo")
public class EchoEndpoint {

    @OnMessage
    public void onMessage(final Session session, final String text) throws IOException {
        session.getBasicRemote().sendText(text);
    }
}
