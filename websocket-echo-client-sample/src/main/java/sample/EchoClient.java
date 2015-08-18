package sample;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class EchoClient {

    static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args)
            throws DeploymentException, IOException, InterruptedException {
        WebSocketContainer container = ContainerProvider
                .getWebSocketContainer();
        try (Session session = container.connectToServer(EchoClient.class,
                URI.create("ws://echo.websocket.org"))) {
            latch.await();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Open");

        String message = "Hello, world!";
        System.out.printf("Send: %s%n", message);
        session.getAsyncRemote().sendText(message);
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.printf("Response: %s%n", message);
        latch.countDown();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Close");
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        System.out.println("Error");
        thr.printStackTrace(System.out);
    }
}
