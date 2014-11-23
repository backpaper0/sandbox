package chat;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

public class ChatMain {

    public static void main(String[] args) throws DeploymentException,
            IOException {
        Map<String, Object> properties = Collections.emptyMap();
        Server server = new Server("localhost", 8080, "/ws", properties,
                ChatEndPoint.class);
        try {
            server.start();
            System.in.read();
        } finally {
            server.stop();
        }
    }
}
