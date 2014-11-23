package chat;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat/{guest-id}")
public class ChatEndPoint {

    private static CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(@PathParam("guest-id") String guestId, Session session) {
        sessions.add(session);
        for (Session s : sessions) {
            s.getAsyncRemote().sendText(guestId + "さんが入室しました");
        }
    }

    @OnMessage
    public void onMessage(@PathParam("guest-id") String guestId, String text) throws
            IOException {
        for (Session s : sessions) {
            s.getAsyncRemote().sendText("[" + guestId + "] " + text);
        }
    }

    @OnClose
    public void onClose(@PathParam("guest-id") String guestId, Session session) {
        sessions.remove(session);
        for (Session s : sessions) {
            s.getAsyncRemote().sendText(guestId + "さんが退室しました");
        }
    }
}
